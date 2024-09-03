package com.sajoproject.sajotuna.user.dto.userSignInDto;

import lombok.Getter;

@Getter
public class SigninResponseDto {
    private final String bearerToken;

    public SigninResponseDto(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
