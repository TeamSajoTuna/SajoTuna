package com.sajoproject.sajotuna.feed.service;


import com.sajoproject.sajotuna.exception.BadRequestException;
import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoRequest;
import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoResponse;
import com.sajoproject.sajotuna.feed.dto.feedGetFeedByIdDto.FeedGetFeedByIdDtoResponse;
import com.sajoproject.sajotuna.feed.entity.Feed;
import com.sajoproject.sajotuna.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    @Transactional
    public FeedGetFeedByIdDtoResponse getFeedById(Long id) {

        Feed feed = feedRepository.findById(id).orElseThrow(() -> new BadRequestException("존재하지 않는 feed_id"));
        FeedGetFeedByIdDtoResponse resFeed = new FeedGetFeedByIdDtoResponse(feed);
        return resFeed;
    }

    @Transactional
    public void getFeedPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);


    }


    @Transactional
    public FeedCreateDtoResponse feedCreate(FeedCreateDtoRequest requestDto) {

        Feed feed = new Feed(requestDto);
        feedRepository.save(feed);
        return new FeedCreateDtoResponse(feed);
    }

}
