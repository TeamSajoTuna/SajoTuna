package com.sajoproject.sajotuna.user.dto.userDeleteDto;

import lombok.Getter;


@Getter
public class DeleteResponseDto {
    private final String message;

    public DeleteResponseDto(String message) {
        this.message = message;
    }
}
