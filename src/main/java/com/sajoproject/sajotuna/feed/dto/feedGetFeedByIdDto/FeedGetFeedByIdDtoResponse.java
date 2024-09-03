package com.sajoproject.sajotuna.feed.dto.feedGetFeedByIdDto;

import com.sajoproject.sajotuna.feed.entity.Feed;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FeedGetFeedByIdDtoResponse {
    Long feedId;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;
    String title;
    String content;
    Long userId;

    public FeedGetFeedByIdDtoResponse(Feed feed){
        this.feedId=feed.getFeedId();
        this.createdAt=feed.getCreatedAt();
        this.modifiedAt=feed.getModifiedAt();
        this.title=feed.getTitle();
        this.content=feed.getContent();
        this.userId=feed.getUser().getUserId();

    }

}
