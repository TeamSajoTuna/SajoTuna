package com.sajoproject.sajotuna.feed.service;

import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoRequest;
import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoResponse;
import com.sajoproject.sajotuna.feed.entity.Feed;
import com.sajoproject.sajotuna.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    @Transactional
    public FeedCreateDtoResponse feedCreate(FeedCreateDtoRequest requestDto) {
        Feed feed = new Feed(requestDto);
        feedRepository.save(feed);
        return new FeedCreateDtoResponse(feed);
    }
}
