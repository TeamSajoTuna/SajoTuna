package com.sajoproject.sajotuna.feed.service;

import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoRequest;
import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoResponse;
import com.sajoproject.sajotuna.feed.dto.feedGetFeedByIdDto.FeedGetFeedByIdDtoResponse;
import com.sajoproject.sajotuna.feed.dto.feedUpdatdDto.FeedUpdateRequestDto;
import com.sajoproject.sajotuna.feed.dto.feedUpdatdDto.FeedUpdateResponseDto;
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

    @Transactional
    public FeedUpdateResponseDto feedUpdate(Long feedId, FeedUpdateRequestDto requestDto) {

        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("ID를 찾을 수 없습니다" + feedId));

        feed.feedUpdate(requestDto.getTitle(), requestDto.getContent());

        feedRepository.save(feed);

        FeedUpdateResponseDto responseDto = new FeedUpdateResponseDto(
                feed.getFeedId(),
                feed.getTitle(),
                feed.getContent(),
                "게시글이 수정되었습니다."
        );
        return responseDto;
    }

    public void feedDelete(Long id) {
        Feed feed = feedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID를 찾을 수 없습니다." + id));

        feedRepository.delete(feed);
    }
}