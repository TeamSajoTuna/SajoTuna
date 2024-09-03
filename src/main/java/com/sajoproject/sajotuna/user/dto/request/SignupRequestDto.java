package com.sajoproject.sajotuna.user.dto.request;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String nickname;
    private String email;
    private String pw;
    private String userRole;
}
