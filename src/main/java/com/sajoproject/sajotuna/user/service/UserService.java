package com.sajoproject.sajotuna.user.service;

import com.sajoproject.sajotuna.config.JwtUtil;
import com.sajoproject.sajotuna.config.PasswordEncoder;
import com.sajoproject.sajotuna.enums.UserRole;
import com.sajoproject.sajotuna.exception.ForbiddenException;
import com.sajoproject.sajotuna.excption.CustomException;
import com.sajoproject.sajotuna.excption.ErrorCode;
import com.sajoproject.sajotuna.user.dto.userSignInDto.SigninRequestDto;
import com.sajoproject.sajotuna.user.dto.userSignupDto.SignupRequestDto;
import com.sajoproject.sajotuna.user.dto.userSignupDto.SignupResponseDto;
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
    public String signup(SignupRequestDto signupRequestDto) {
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
                ()->new CustomException(ErrorCode.USER_NOT_FOUND));
        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401 반환
        if(!passwordEncoder.matches(requestDto.getPw(), user.getPw())) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return jwtUtil.createToken(
                user.getUserId(),
                user.getNickname(),
                user.getEmail(),
                user.getUserRole()
        );
    }

    @Transactional
    public void deleteUser(Long userId, HttpServletRequest request) {
        // 권한 검증 후 userId 찾기 ->
        User user = userRepository.findById(userId).orElseThrow(()->
                new NoSuchElementException("not found user"));
        // JWT 토큰 헤더에서 추출
        String bearerToken = request.getHeader("Authorization");
        String jwt = jwtUtil.substringToken(bearerToken);

        // 토큰에서 Claim 추출
        Claims claims = jwtUtil.extractClaims(jwt);
        String userRole = claims.get("userRole", String.class);

        // ADMIN 권한을 가진 사용자인 경우에만 삭제 가능
        if(!UserRole.ADMIN.name().equalsIgnoreCase(userRole)) {
            throw new ForbiddenException("오류");
        }

        userRepository.delete(user);
    }
}
