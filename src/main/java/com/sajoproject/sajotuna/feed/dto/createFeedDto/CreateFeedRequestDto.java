package com.sajoproject.sajotuna.feed.dto.createFeedDto;

import lombok.Getter;

@Getter
public class CreateFeedRequestDto {
    private String title;
    private String content;
    private Long userId;
}
