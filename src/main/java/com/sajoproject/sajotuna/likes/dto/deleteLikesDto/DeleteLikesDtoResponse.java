package com.sajoproject.sajotuna.likes.dto.deleteLikesDto;

import lombok.Getter;

@Getter
public class DeleteLikesDtoResponse {
    private final String message;


    public DeleteLikesDtoResponse(String message) {
        this.message = message;
    }
}
