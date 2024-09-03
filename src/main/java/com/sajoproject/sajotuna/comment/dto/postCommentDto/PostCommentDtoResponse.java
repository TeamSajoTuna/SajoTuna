package com.sajoproject.sajotuna.comment.dto.postCommentDto;

import com.sajoproject.sajotuna.comment.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostCommentDtoResponse {

    private Long commentId;
    private String content;
    private Long feedId;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostCommentDtoResponse(Comment comment){
        this.commentId=comment.getCommentId();
        this.content=comment.getContent();
        this.feedId=comment.getFeed().getFeedId();
        this.userId=comment.getUser().getUserId();
        this.createdAt=comment.getCreatedAt();
        this.modifiedAt=comment.getModifiedAt();

    }

}
