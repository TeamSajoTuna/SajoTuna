package com.sajoproject.sajotuna.feed.dto.feedDeleteDto;

import lombok.Getter;

@Getter
public class FeedDeleteResponseDto {
    private final String message;

    public FeedDeleteResponseDto(String message) {
        this.message = message;
    }
}