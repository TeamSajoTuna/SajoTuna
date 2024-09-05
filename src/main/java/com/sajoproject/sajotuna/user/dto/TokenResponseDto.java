package com.sajoproject.sajotuna.user.dto;

import lombok.Getter;

@Getter
public class TokenResponseDto {
    private final String accessToken;
    private final String refreshToken;

    public TokenResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
