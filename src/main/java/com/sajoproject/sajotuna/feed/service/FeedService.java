package com.sajoproject.sajotuna.feed.service;

import com.sajoproject.sajotuna.feed.dto.feedUpdatdDto.FeedUpdateRequestDto;
import com.sajoproject.sajotuna.feed.dto.feedUpdatdDto.FeedUpdateResponseDto;
import com.sajoproject.sajotuna.feed.entity.Feed;
import com.sajoproject.sajotuna.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;

    public FeedUpdateResponseDto feedUpdate(Long feedId, FeedUpdateRequestDto requestDto) {

        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("ID를 찾을 수 없습니다" + feedId));

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
