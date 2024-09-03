package com.sajoproject.sajotuna.user.dto.userUpdateProfileDto;

import lombok.Getter;

@Getter

public class UpdateRequestDto {
    private Long userId;
    private String pw;
    private String nickname;
    private String email;

}
