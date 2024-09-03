package com.sajoproject.sajotuna.comment.dto.postCommentDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentDtoRequest {
    private String content;
    private Long feedId;
    private Long userId;


}
