package com.sajoproject.sajotuna.config;

import com.sajoproject.sajotuna.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
// JWT 토큰 생성, 파싱, 유효성 검증
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 10 * 1000L; // 10초
    private static final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L;  // 7일

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /*
    JWT 서명에 사용할 'KEY' 초기화,
    secretKey: Base64로 인코딩 된 키 문자열 -> 이를 디코딩해 HMAC SHA 키로 반환
     */
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // Access Token 생성
    public String createAccessToken(Long userId, String username, String email, UserRole userRole) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("username", username)
                        .claim("email", email)
                        .claim("userRole", userRole.name())
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact(); // JWT 문자열 반환
    }

    // Refresh Token 생성
    public String createRefreshToken(Long userId){
        Date date = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }


// JWT 추출
    public String substringToken(String tokenValue) {
        // 'Bearer' 로 시작하는 토큰 문자열에서 실제 JWT 추출
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new NullPointerException("Not Found Token");
    }

    // JWT 토큰에서 Claims 추출
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT 토큰 검증
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 예외 발생 시 로그 남기기, 보안조치(경고 알림, 사용자계정 잠금)
            log.error("jwt 토큰 검증 실패: {}", e.getMessage());
            return false;
        }
    }

//    사용자 ID jwt토큰 추출
    public String getUserId(String token) {
        return extractClaims(token).getSubject();
    }
}