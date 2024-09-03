package com.sajoproject.sajotuna.feed.dto.createFeedDto;

import com.sajoproject.sajotuna.feed.entity.Feed;
import com.sajoproject.sajotuna.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateFeedResponseDto {
    private Long feedId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long userId;

    public CreateFeedResponseDto(Feed feed) {
        this.feedId = feed.getFeedId();
        this.title = feed.getTitle();
        this.content = feed.getContent();
        this.createdAt = feed.getCreatedAt();
        this.modifiedAt = feed.getModifiedAt();

        User user = new User();
        user.setUserId(feed.getUser().getUserId());
        this.userId = user.getUserId();
    }
}
