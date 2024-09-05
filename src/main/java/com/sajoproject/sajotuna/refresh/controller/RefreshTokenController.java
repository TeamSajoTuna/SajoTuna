package com.sajoproject.sajotuna.refresh.controller;

import com.sajoproject.sajotuna.refresh.service.RefreshTokenService;
import com.sajoproject.sajotuna.user.dto.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/token/refresh")
    public ResponseEntity<Void> refreshAccessToken(@RequestHeader("RefreshToken")String refreshToken){
        TokenResponseDto tokenResponseDto = refreshTokenService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok()
                .header("Authorization", tokenResponseDto.getAccessToken())
                .header("RefreshToken", tokenResponseDto.getRefreshToken())
                .build();
    }
}
