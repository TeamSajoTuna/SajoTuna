package com.sajoproject.sajotuna.user.dto.userGetProfileDto;

import lombok.Getter;

@Getter
public class GetProfileResponseDto {
    private final Long userId;
    private final String nickname;
    private final String email;

    public GetProfileResponseDto(Long userId, String nickname, String email) {
        this.userId=userId;
        this.nickname = nickname;
        this.email = email;
    }
}
