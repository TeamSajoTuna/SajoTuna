package com.sajoproject.sajotuna.user.dto.userUpdateProfileDto;

import lombok.Getter;

@Getter

public class UpdateResponseDto {
    private final Long userId;
    private final String pw;
    private final String nickname;
    private final String email;

    public UpdateResponseDto(Long userId, String pw, String nickname, String email) {
        this.userId = userId;
        this.pw = pw;
        this.nickname = nickname;
        this.email = email;
    }
}
