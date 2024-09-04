package com.sajoproject.sajotuna.feed.service;

import com.sajoproject.sajotuna.exception.BadRequestException;
import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoRequest;
import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoResponse;
import com.sajoproject.sajotuna.feed.dto.feedGetFeedByIdDto.FeedGetFeedByIdDtoResponse;
import com.sajoproject.sajotuna.feed.dto.feedPagingDto.FeedPagingDtoResponse;
import com.sajoproject.sajotuna.feed.dto.feedUpdatdDto.FeedUpdateRequestDto;
import com.sajoproject.sajotuna.feed.dto.feedUpdatdDto.FeedUpdateResponseDto;
import com.sajoproject.sajotuna.feed.entity.Feed;
import com.sajoproject.sajotuna.feed.repository.FeedRepository;
import com.sajoproject.sajotuna.following.entity.Follow;
import com.sajoproject.sajotuna.following.repository.FollowingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final FollowingRepository followingRepository;

    @Transactional
    public FeedGetFeedByIdDtoResponse getFeedById(Long id) {

        Feed feed = feedRepository.findById(id).orElseThrow(() -> new BadRequestException("존재하지 않는 feed_id"));
        FeedGetFeedByIdDtoResponse resFeed = new FeedGetFeedByIdDtoResponse(feed);
        return resFeed;
    }

    @Transactional
    public Page<FeedPagingDtoResponse> getFeedPaging(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size);

        // 1. User가 팔로우한 Follow 리스트 조회
        List<Follow> follows = followingRepository.findByFollowing_UserId(userId);

        // 2. Follow 리스트에서 followed_id 추출
        List<Long> followedIds = follows.stream()
                .map(follow -> follow.getFollowed().getUserId())
                .collect(Collectors.toList());

        // 3. followed_id에 해당하는 피드 리스트를 modifiedAt 기준으로 정렬하여 조회
        Page<Feed> feeds = feedRepository.findByUser_UserIdInOrderByModifiedAtDesc(followedIds, pageable);

        // 4. Feed 리스트를 PagingFeedDto로 변환하여 반환
        return feeds.map(FeedPagingDtoResponse::new);
    }


    @Transactional
    public FeedCreateDtoResponse feedCreate(FeedCreateDtoRequest requestDto) {

        Feed feed = new Feed(requestDto);
        feedRepository.save(feed);
        return new FeedCreateDtoResponse(feed);
    }

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