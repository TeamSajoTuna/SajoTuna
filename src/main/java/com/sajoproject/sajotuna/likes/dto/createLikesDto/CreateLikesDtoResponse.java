package com.sajoproject.sajotuna.likes.dto.createLikesDto;

import com.sajoproject.sajotuna.likes.entity.Likes;
import lombok.Getter;

@Getter
public class CreateLikesDtoResponse {
    private Long userId;
    private Long feedId;

    public CreateLikesDtoResponse(Likes likes) {
        this.userId = likes.getUser().getUserId();
        this.feedId = likes.getFeed().getFeedId();
    }
}
