package com.sajoproject.sajotuna.user.service;

import com.sajoproject.sajotuna.config.JwtUtil;
import com.sajoproject.sajotuna.config.PasswordEncoder;
import com.sajoproject.sajotuna.enums.UserRole;
import com.sajoproject.sajotuna.excption.UserException;
import com.sajoproject.sajotuna.user.dto.userGetProfileDto.GetProfileResponseDto;
import com.sajoproject.sajotuna.user.dto.userSignInDto.SigninRequestDto;
import com.sajoproject.sajotuna.user.dto.userSignupDto.SignupRequestDto;
import com.sajoproject.sajotuna.user.dto.userSignupDto.SignupResponseDto;
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
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new IllegalArgumentException("중복된 이메일입니다.");
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

        String bearerToken = jwtUtil.createToken(
                savedUser.getUserId(),
                savedUser.getNickname(),
                savedUser.getEmail(),
                savedUser.getUserRole()
        );

        return new SignupResponseDto(bearerToken);
    }

    private UserRole convertToUserRole(String userRoleString) {
        try {
            return UserRole.valueOf(userRoleString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 역할 정보입니다: " + userRoleString);
        }
    }


    // Signin
    public String signIn(SigninRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                ()->new IllegalArgumentException("not found email"));

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401 반환
        if(!passwordEncoder.matches(requestDto.getPw(), user.getPw())) {
            throw new UserException("incorrect password");
        }
        return jwtUtil.createToken(
                user.getUserId(),
                user.getNickname(),
                user.getEmail(),
                user.getUserRole()
        );
    }

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
        if (updateRequestDto.getCurrentPassword() != null && updateRequestDto.getNewPassword() != null) {
            String currentPassword = user.getPw();
            String newPassword = updateRequestDto.getNewPassword();

//            비밀번호 형식 확인
            if (!isValidPasswordFormat(newPassword)){
                throw new IllegalArgumentException("비밀번호 형식이 맞지 않습니다.");
            }
//            동일한 비밀번호로 변경 하는지 확인
            if (passwordEncoder.matches(newPassword, currentPassword)){
                throw new IllegalArgumentException("현재 패스워드와 변경하려는 패스워드가 같습니다.");
            }
//            새로바꾼 비밀번호와 입력한비밀번호가 동일한지 확인
            if(!passwordEncoder.matches(updateRequestDto.getCurrentPassword(), currentPassword)) {
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

    @Transactional
    public void deleteUser(Long userId, HttpServletRequest request) {
        // 권한 검증 후 userId 찾기 ->
        User user = userRepository.findById(userId).orElseThrow(()->
                new NoSuchElementException("not found user"));
        // JWT 토큰 헤더에서 추출
        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
//            log.error("JWT 토큰 x");
//            throw new IllegalArgumentException("you need to be in token");
//        }
        String jwt = jwtUtil.substringToken(bearerToken);

//        log.debug("JWT Token: {}", bearerToken);
//        log.debug("Extract JWT: {}", jwt);

        // 토큰에서 Claim 추출
        Claims claims = jwtUtil.extractClaims(jwt);
//        try{
//            claims = jwtUtil.extractClaims(jwt);
//        } catch (Exception e) {
//            log.error("jwt 토큰 처리 오류", e);
//            throw new SecurityException("JWT 토큰 처리 중 오류 발생",e);
//        }
        String userRole = claims.get("userRole", String.class);

        // 자신의 계정이나 ADMIN 권한을 가진 사용자인 경우에만 삭제 가능
        if(!UserRole.ADMIN.name().equalsIgnoreCase(userRole)) {
            throw new UserException("해당 계정을 삭제할 권한이 없습니다.");
        }

        userRepository.delete(user);
    }


}
