package com.sajoproject.sajotuna.user.dto.userSignupDto;

import lombok.Getter;

@Getter
public class SignupResponseDto {
    private final String bearerToken;


    public SignupResponseDto(String bearerToken) {this.bearerToken = bearerToken;}
}
