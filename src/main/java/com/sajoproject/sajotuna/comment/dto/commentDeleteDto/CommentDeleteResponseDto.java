package com.sajoproject.sajotuna.comment.dto.commentDeleteDto;

import lombok.Getter;

@Getter
public class CommentDeleteResponseDto {
    private String message;

    public CommentDeleteResponseDto(String message) {
        this.message = message;
    }
}
