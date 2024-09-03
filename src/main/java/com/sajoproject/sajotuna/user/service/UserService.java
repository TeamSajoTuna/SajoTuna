package com.sajoproject.sajotuna.user.service;

import com.sajoproject.sajotuna.config.JwtUtil;
import com.sajoproject.sajotuna.config.PasswordEncoder;
import com.sajoproject.sajotuna.enums.UserRole;
import com.sajoproject.sajotuna.exception.Conflict;
import com.sajoproject.sajotuna.exception.UnAuthorized;
import com.sajoproject.sajotuna.exception.Forbidden;
import com.sajoproject.sajotuna.exception.UserNotFoundException;
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

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    // Signup
    @Transactional
    public String signup(SignupRequestDto signupRequestDto) {
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
        User savedUser = userRepository.save(newUser);
        return jwtUtil.createToken(savedUser.getUserId(), savedUser.getNickname(), savedUser.getEmail(), savedUser.getUserRole());
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
    public String signIn(SigninRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                ()-> new UserNotFoundException("not found"));
        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401 반환
        if(!(user.getEmail().equalsIgnoreCase(requestDto.getEmail())) || !passwordEncoder.matches(requestDto.getPw(), user.getPw())) {
            throw new UnAuthorized("incorrect email or password");
        }
        return jwtUtil.createToken(
                user.getUserId(),
                user.getNickname(),
                user.getEmail(),
                user.getUserRole()
        );
    }
// ==============================================================================================
//    프로필 조회  getProfile
    public GetProfileResponseDto getProfile(Long userId, boolean isOwnProfile) {
       User user = userRepository.findById(userId).orElseThrow(()-> new NullPointerException("not found userId"));

//       본인 프로필이면 Id, 닉네임, 이메일 반환, 아니라면 Id,닉네임만 반환
       if (isOwnProfile){
           return new GetProfileResponseDto(
                   user.getUserId(),
                   user.getNickname(),
                   user.getEmail());
       } else {
           return new GetProfileResponseDto(
                   user.getUserId(),
                   user.getNickname(),
                   null);
       }
    }


//    프로필 수정 updateProfile
    @Transactional
    public UpdateResponseDto updateProfile(Long userId, UpdateRequestDto updateRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(()-> new NullPointerException("not found userId"));


//            비밀번호 수정
        if (updateRequestDto.getPw() != null){
            String currentPassword =  user.getPw();
            String newPassword = updateRequestDto.getPw();

//            비밀번호 형식 확인
            if (!isValidPasswordFormat(newPassword)){
                throw new IllegalArgumentException("비밀번호 형식이 맞지 않습니다.");
            }
//            동일한 비밀번호로 변경 하는지 확인
            if (passwordEncoder.matches(newPassword, currentPassword)){
                throw new IllegalArgumentException("현재 패스워드와 변경하려는 패스워드가 같습니다.");
            }
//            새로바꾼 비밀번호와 입력한비밀번호가 동일한지 확인
            if(!passwordEncoder.matches(updateRequestDto.getPw(), currentPassword)) {
                throw new IllegalArgumentException("변경된 비밀번호가 현재 입력한 비밀번호와 다릅니다.");
            }
//            비밀번호 변경
            user.updatePw(passwordEncoder.encode(user.getPw()));
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
/*
*   패스워드 형식
* - 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함합니다.
  - 비밀번호는 최소 8글자 이상이어야 합니다
*/
    private boolean isValidPasswordFormat(String pw){
        if (pw.length()<8){
            return false;
        }
        boolean hasUpperCase = pw.chars().anyMatch(Character::isUpperCase);
        boolean hasLowerCase = pw.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = pw.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = pw.chars().anyMatch(ch -> "!@#$%^&*()-+=<>?~".indexOf(ch) >= 0);

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }
// ==============================================================================================
    @Transactional
    public void deleteUser(Long userId, HttpServletRequest request) {
        // 권한 검증 후 userId 찾기 ->
        User user = userRepository.findById(userId).orElseThrow(()->
                new UserNotFoundException("not found user"));
        // JWT 토큰 헤더에서 추출
        String bearerToken = request.getHeader("Authorization");
        String jwt = jwtUtil.substringToken(bearerToken);

        // 토큰에서 Claim 추출
        Claims claims = jwtUtil.extractClaims(jwt);
        String userRole = claims.get("userRole", String.class);

        // ADMIN 권한을 가진 사용자인 경우에만 삭제 가능
        if(!UserRole.ADMIN.name().equalsIgnoreCase(userRole)) {
            throw new Forbidden("당신은 ADMIN 유저가 아닙니다.");
        }

        user.setIsDeleted(true);
        userRepository.save(user);
    }


}
