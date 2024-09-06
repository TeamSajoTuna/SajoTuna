package com.sajoproject.sajotuna.feed.dto.feedLikeDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedLikeCountResponseDto {
    private Long feedId;
    private Long likeCount;

    public FeedLikeCountResponseDto(Long feedId, Long likeCount) {
        this.feedId = feedId;
        this.likeCount = likeCount;
    }
}
