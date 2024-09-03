package com.sajoproject.sajotuna.comment.dto.getCommentFromFeedDto;

import com.sajoproject.sajotuna.comment.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetCommentFromFeedDtoResponse {
    Long commentId;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;
    String content;
    Long feedId;
    Long userId;

    public GetCommentFromFeedDtoResponse(Comment comment){
        this.commentId=comment.getCommentId();
        this.createdAt=comment.getCreatedAt();
        this.modifiedAt=comment.getModifiedAt();
        this.content=comment.getContent();
        this.feedId=comment.getFeed().getFeedId();
        this.userId=comment.getUser().getUserId();

    }
}
