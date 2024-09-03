package com.sajoproject.sajotuna.user.dto.userSignupDto;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String nickname;
    private String email;
    private String pw;
    private String userRole;
}
