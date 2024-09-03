package com.sajoproject.sajotuna.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;


@Getter
public class DeleteResponseDto {
    private final String message;

    public DeleteResponseDto(String message) {
        this.message = message;
    }
}
