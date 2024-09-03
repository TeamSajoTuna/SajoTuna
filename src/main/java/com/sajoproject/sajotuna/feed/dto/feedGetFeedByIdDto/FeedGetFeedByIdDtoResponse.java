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
        this.feedId=getFeedId();
        this.createdAt=getCreatedAt();
        this.modifiedAt=getModifiedAt();
        this.title=getTitle();
        this.content=getContent();
        this.userId=feed.getUser().getUserId();

    }

}
