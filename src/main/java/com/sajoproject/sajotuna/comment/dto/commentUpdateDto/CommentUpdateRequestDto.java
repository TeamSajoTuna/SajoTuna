package com.sajoproject.sajotuna.comment.dto.commentUpdateDto;

import lombok.Getter;

@Getter
public class CommentUpdateRequestDto {
    private String content;
    private Boolean isReply;
}
