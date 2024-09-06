package com.sajoproject.sajotuna.feed.service;

import com.sajoproject.sajotuna.exception.BadRequestException;
import com.sajoproject.sajotuna.exception.MethodNotAllowed;
import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoRequest;
import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoResponse;
import com.sajoproject.sajotuna.feed.dto.feedGetFeedByIdDto.FeedGetFeedByIdDtoResponse;
import com.sajoproject.sajotuna.feed.dto.feedLikeDto.CountDto;
import com.sajoproject.sajotuna.feed.dto.feedLikeDto.FeedLikeCountResponseDto;
import com.sajoproject.sajotuna.feed.dto.feedPagingDto.FeedPagingDtoResponse;
import com.sajoproject.sajotuna.feed.dto.feedUpdatdDto.FeedUpdateRequestDto;
import com.sajoproject.sajotuna.feed.dto.feedUpdatdDto.FeedUpdateResponseDto;
import com.sajoproject.sajotuna.feed.entity.Feed;
import com.sajoproject.sajotuna.feed.repository.FeedRepository;
import com.sajoproject.sajotuna.following.entity.Follow;
import com.sajoproject.sajotuna.following.repository.FollowingRepository;
import com.sajoproject.sajotuna.likes.repository.LikesRepository;
import com.sajoproject.sajotuna.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final FollowingRepository followingRepository;
    private final LikesRepository likesRepository;

    @Transactional
    public FeedGetFeedByIdDtoResponse getFeedById(Long id) {

        Feed feed = feedRepository.findById(id).orElseThrow(() -> new BadRequestException("존재하지 않는 feed_id"));

        if (feed.getUser() ==null){
            throw new BadRequestException("Feed 객체의 User 정보가 없습니다.");
        }

        if (feed.getIsDeleted()){
            throw new MethodNotAllowed("게시물이 삭제되어 조회가 불가능 합니다.");
        }

        feed.setViewCount(feed.getViewCount()+1);

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
        Page<Feed> feeds = feedRepository.findByUser_UserIdInAndIsDeletedFalseOrderByModifiedAtDesc(followedIds, pageable);

        // 4. Feed 리스트를 PagingFeedDto로 변환하여 반환
        return feeds.map(FeedPagingDtoResponse::new);
    }


    @Transactional
    public FeedCreateDtoResponse feedCreate(FeedCreateDtoRequest requestDto, Long userId) {
        User user = new User();
        user.setUserId(userId);

        Feed feed = new Feed(requestDto, user);
        feedRepository.save(feed);

        return new FeedCreateDtoResponse(feed);
    }

    // 게시물 수정
    @Transactional
    public FeedUpdateResponseDto feedUpdate(
            Long feedId, FeedUpdateRequestDto requestDto, Long currentUserId) {

        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시물을 찾을 수 없습니다" + feedId));

        if (feed.getIsDeleted()){
            throw new MethodNotAllowed("이미 삭제된 게시물 입니다.");
        }

        if (!feed.getUser().getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("게시물 수정 권한이 없습니다.");
        }

        feed.feedUpdate(requestDto.getTitle(), requestDto.getContent());
        feedRepository.save(feed);

        return new FeedUpdateResponseDto(
                feed.getFeedId(),
                feed.getTitle(),
                feed.getContent(),
                "게시물이 수정되었습니다."
        );
    }

    // 게시물 삭제
    @Transactional
    public void feedDelete(Long id, Long currentUserId) {
        Feed feed = feedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시물을 찾을 수 없습니다." + id));

        if (!feed.getUser().getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("게시물 삭제 권한이 없습니다.");
        }

        feed.setIsDeleted(true);
        feedRepository.save(feed);
    }

    //   인기게시물 좋아요
    @Transactional(readOnly = true)
    public List<FeedPagingDtoResponse> getTop10Feeds() {
        Pageable pageable = PageRequest.of(0, 10); // 페이지 번호 0, 페이지 크기 10
        List<Feed> topFeeds = feedRepository.findTop10Feeds(pageable);
        List<FeedPagingDtoResponse> topFeedList = new ArrayList<>();

        for (Feed feed : topFeeds) {
            FeedPagingDtoResponse dto = new FeedPagingDtoResponse(feed);
            long likeCount = likesRepository.countByFeed(feed); // 좋아요 수 계산
            dto.setLikeCount(likeCount);
            topFeedList.add(dto);
        }
        return topFeedList;
    }

    // 한 피드의 모든 좋아요 수
    @Transactional(readOnly = true)
    public FeedLikeCountResponseDto getLikeCountByFeedId(Long feedId) {
        Optional<CountDto> result = feedRepository.countLikesByFeedId(feedId);
        if (result == null) {
            throw new BadRequestException("게시글을 찾을 수 없습니다.");
        }
        Long likeCount = result.get().getCnt();
        return new FeedLikeCountResponseDto(feedId, likeCount);
    }


}