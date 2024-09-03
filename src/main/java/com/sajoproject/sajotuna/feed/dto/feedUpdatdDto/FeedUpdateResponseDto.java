package com.sajoproject.sajotuna.feed.dto.feedUpdatdDto;

import lombok.Getter;

@Getter
public class FeedUpdateResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String message;

    public FeedUpdateResponseDto(Long id, String title, String content, String message) {
        this.id = id;
        this.title = title;
        this.content  = content;
        this.message = message;
    }

}
