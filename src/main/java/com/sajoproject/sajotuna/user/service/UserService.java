package com.sajoproject.sajotuna.user.service;

import com.sajoproject.sajotuna.config.JwtUtil;
import com.sajoproject.sajotuna.config.PasswordEncoder;
import com.sajoproject.sajotuna.enums.UserRole;
import com.sajoproject.sajotuna.exception.Conflict;
import com.sajoproject.sajotuna.exception.Forbidden;
import com.sajoproject.sajotuna.exception.UnAuthorized;
import com.sajoproject.sajotuna.exception.UserNotFoundException;
import com.sajoproject.sajotuna.feed.dto.feedLikeDto.FeedLikeCountResponseDto;
import com.sajoproject.sajotuna.feed.entity.Feed;
import com.sajoproject.sajotuna.feed.repository.FeedRepository;
import com.sajoproject.sajotuna.feed.service.FeedService;
import com.sajoproject.sajotuna.refresh.entity.RefreshToken;
import com.sajoproject.sajotuna.refresh.repository.RefreshTokenRepository;
import com.sajoproject.sajotuna.user.dto.TokenResponseDto;
import com.sajoproject.sajotuna.user.dto.authUserDto.AuthUser;
import com.sajoproject.sajotuna.user.dto.userGetProfileDto.GetProfileResponseDto;
import com.sajoproject.sajotuna.user.dto.userSignInDto.SigninRequestDto;
import com.sajoproject.sajotuna.user.dto.userSignupDto.SignupRequestDto;
import com.sajoproject.sajotuna.user.dto.userUpdateProfileDto.UpdateRequestDto;
import com.sajoproject.sajotuna.user.dto.userUpdateProfileDto.UpdateResponseDto;
import com.sajoproject.sajotuna.user.entity.User;
import com.sajoproject.sajotuna.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final FeedRepository feedRepository;
    private final FeedService feedService;

    // Signup
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new Conflict("중복된 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPw());
        UserRole userRole = convertToUserRole(signupRequestDto.getUserRole());
        User newUser = new User(
                signupRequestDto.getNickname(),
                signupRequestDto.getEmail(),
                encodedPassword,
                userRole
        );
        userRepository.save(newUser);
    }


    private UserRole convertToUserRole(String userRoleString) {
        try {
            return UserRole.valueOf(userRoleString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 역할 정보입니다: " + userRoleString);
        }
    }

    // SignIn
    @Transactional
    public TokenResponseDto signIn(SigninRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new UserNotFoundException("not found"));
        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401 반환
        if (!(user.getEmail().equalsIgnoreCase(requestDto.getEmail())) || !passwordEncoder.matches(requestDto.getPw(), user.getPw())) {
            throw new UnAuthorized("incorrect email or password");
        }
        // Access token 및 refresh token 생성
        String accessToken = jwtUtil.createAccessToken(
                user.getUserId(),
                user.getNickname(),
                user.getEmail(),
                user.getUserRole());

        String refreshToken = jwtUtil.createRefreshToken(
                user.getUserId());

        // Refresh Token을 DB에 저장하는 로직
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUserId(user.getUserId())
                .orElseGet(() ->new RefreshToken(user.getUserId(), user.getNickname(), user.getEmail(), user.getUserRole(), refreshToken));

        refreshTokenRepository.save(refreshTokenEntity);
        return new TokenResponseDto(accessToken,refreshToken);
    }

    // 프로필 조회  getProfile
    // 자신이 작성한 피드의 총 좋아요 갯수로 인한 등급 포함
    @Transactional
    public GetProfileResponseDto getProfile(Long userId, boolean isOwnProfile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NullPointerException("not found userId"));

        String grade = userGrade(userId);

        //본인 프로필이면 Id, 닉네임, 이메일, 좋아요등급 반환, 아니라면 Id,닉네임만 반환
        if (isOwnProfile) {
            return new GetProfileResponseDto(
                    user.getUserId(),
                    user.getNickname(),
                    user.getEmail(),
                    grade);
        } else {
            return new GetProfileResponseDto(
                    user.getUserId(),
                    user.getNickname(),
                    null,
                    null);
        }
    }

    //    프로필 수정 updateProfile
    @Transactional
    public UpdateResponseDto updateProfile(Long userId, UpdateRequestDto updateRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("not found userId"));

//            비밀번호 수정
        if (updateRequestDto.getCurrentPassword() != null && updateRequestDto.getNewPassword() != null) {
            String currentPassword = user.getPw();
            String newPassword = updateRequestDto.getNewPassword();


//            동일한 비밀번호로 변경 하는지 확인
            if (passwordEncoder.matches(newPassword, currentPassword)) {
                throw new IllegalArgumentException("현재 패스워드와 변경하려는 패스워드가 같습니다.");
            }
//            새로바꾼 비밀번호와 입력한비밀번호가 동일한지 확인
            if (!passwordEncoder.matches(updateRequestDto.getCurrentPassword(), currentPassword)) {
                throw new IllegalArgumentException("변경된 비밀번호가 현재 입력한 비밀번호와 다릅니다.");
            }
//            비밀번호 변경
            user.updatePw(passwordEncoder.encode(newPassword));
        }
//        닉네임, 이메일 변경
        user.updateProfile(updateRequestDto.getNickname(), updateRequestDto.getEmail());

        User updatedUser = userRepository.save(user);
        return new UpdateResponseDto(
                updatedUser.getUserId(),
                updatedUser.getPw(),
                updatedUser.getNickname(),
                updatedUser.getEmail());
    }



    @Transactional
    public void deleteUser(Long userId, AuthUser user) {
        // 권한 검증 후 userId 찾기
        User userToDelete = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("not found user"));

        // ADMIN 권한을 가진 사용자인 경우에만 삭제 가능
        if (!UserRole.ADMIN.name().equalsIgnoreCase(user.getUserRole())) {
            throw new Forbidden("당신은 ADMIN 유저가 아닙니다.");
        }

        userToDelete.setIsDeleted(true);
        userRepository.save(userToDelete);
    }


    // 자신이 작성한 피드의 총 좋아요 갯수로 인한 등급 산정
    @Transactional
    public String userGrade(Long userId) {
        //만약 이 유저가 작성한 피드가 없을 경우 - 기본 STAR1
        User user = userRepository.findById(userId).orElseThrow();
        if(!feedRepository.existsByUser(user)) {
            return "Star1";
        }

        List<Feed> feeds = feedRepository.findByUser(user);
        int sum = 0;
        // FeedService의 메서드를 호출하여 좋아요 수를 가져옴
        for (Feed feed : feeds) {
            FeedLikeCountResponseDto likeCountDto = feedService.getLikeCountByFeedId(feed.getFeedId());
            sum += likeCountDto.getLikeCount();
        }

        //2개 이하(0,1,2) - Star1 / 2개 초과 5개 이하(3,4,5) - Star2 / 5개 초과(6~) - Star3
        String grade;
        if (sum <= 2) {
            grade = "Star1";
        } else if (sum <= 5) {
            grade = "Star2";
        } else {
            grade = "Star3";
        }
        return grade;
    }

}
