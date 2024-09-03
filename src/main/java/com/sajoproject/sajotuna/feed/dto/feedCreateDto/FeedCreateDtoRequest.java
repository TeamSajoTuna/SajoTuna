package com.sajoproject.sajotuna.feed.dto.feedCreateDto;

import lombok.Getter;

@Getter
public class FeedCreateDtoRequest {
    private String title;
    private String content;
    private Long userId;
}
