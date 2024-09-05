package com.sajoproject.sajotuna.comment.dto.commentUpdateDto;

import lombok.Getter;

@Getter
public class CommentUpdateResponseDto {
    private Long commentId;
    private String content;
    private Long feedId;
    private Long userId;
    private Long replyCommentId;

    public CommentUpdateResponseDto(
            Long commentId, String content, Long feedId, Long userId, Long replyCommentId) {
        this.commentId = commentId;
        this.content = content;
        this.feedId = feedId;
        this.userId = userId;
        this.replyCommentId = replyCommentId;
    }
}
