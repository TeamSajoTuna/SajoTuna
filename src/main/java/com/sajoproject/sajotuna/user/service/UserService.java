package com.sajoproject.sajotuna.user.service;

import com.sajoproject.sajotuna.config.JwtUtil;
import com.sajoproject.sajotuna.config.PasswordEncoder;
import com.sajoproject.sajotuna.enums.UserRole;
import com.sajoproject.sajotuna.excption.UserException;
import com.sajoproject.sajotuna.user.dto.request.SigninRequestDto;
import com.sajoproject.sajotuna.user.dto.request.SignupRequestDto;
import com.sajoproject.sajotuna.user.dto.response.SigninResponseDto;
import com.sajoproject.sajotuna.user.dto.response.SignupResponseDto;
import com.sajoproject.sajotuna.user.entity.User;
import com.sajoproject.sajotuna.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
