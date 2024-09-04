package com.sajoproject.sajotuna.comment.dto.commentUpdateDto;

import lombok.Getter;

@Getter
public class CommentUpdateResponseDto {
    private Long commentId;
    private String content;
    private Long feedId;
    private Long userId;

    public CommentUpdateResponseDto(Long commentId, String content, Long userId, Long feedId) {
        this.commentId = commentId;
        this.content = content;
        this.feedId = feedId;
        this.userId = userId;
    }
}
