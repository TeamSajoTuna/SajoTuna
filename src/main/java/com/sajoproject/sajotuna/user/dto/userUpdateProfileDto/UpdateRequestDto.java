package com.sajoproject.sajotuna.user.dto.userUpdateProfileDto;

import lombok.Getter;

@Getter

public class UpdateRequestDto {
    private Long userId;
    private String currentPassword;
    private String nickname;
    private String email;
    private String newPassword;

}
