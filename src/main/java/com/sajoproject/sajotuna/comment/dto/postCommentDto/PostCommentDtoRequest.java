package com.sajoproject.sajotuna.comment.dto.postCommentDto;

import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.comments.CommentType;

@Getter
@Setter
public class PostCommentDtoRequest {
    private String content;
    private Long feedId;
    private Long userId;
    private Long replyCommentId;
}
