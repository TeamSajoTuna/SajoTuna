package com.sajoproject.sajotuna.refresh.service;

import com.sajoproject.sajotuna.config.JwtUtil;
import com.sajoproject.sajotuna.exception.UnAuthorized;
import com.sajoproject.sajotuna.refresh.entity.RefreshToken;
import com.sajoproject.sajotuna.refresh.repository.RefreshTokenRepository;
import com.sajoproject.sajotuna.user.dto.TokenResponseDto;
import com.sajoproject.sajotuna.user.entity.User;
import com.sajoproject.sajotuna.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public TokenResponseDto refreshAccessToken( String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)){
            throw new UnAuthorized("토큰 업써");
        }
        String userIdString = jwtUtil.getUserId(refreshToken);
        Long userId = Long.parseLong(userIdString);

        RefreshToken storedRefreshToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new UnAuthorized("Refresh Token not found"));
        if(!storedRefreshToken.getToken().equals(refreshToken)){
            throw new UnAuthorized("Refresh Token invalid");
        }

        String newAccessToken = jwtUtil.createAccessToken(userId, storedRefreshToken.getNickName(), storedRefreshToken.getEmail(), storedRefreshToken.getUserRole());
        String newRefreshToken =jwtUtil.createRefreshToken(userId);

        storedRefreshToken.setToken(newRefreshToken);
        refreshTokenRepository.save(storedRefreshToken);
        return new TokenResponseDto(newAccessToken,newRefreshToken);

    }
}
