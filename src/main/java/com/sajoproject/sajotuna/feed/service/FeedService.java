package com.sajoproject.sajotuna.feed.service;

import com.sajoproject.sajotuna.exception.BadRequestException;
import com.sajoproject.sajotuna.exception.MethodNotAllowed;
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
import com.sajoproject.sajotuna.user.entity.User;
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

//    @Transactional
//    public FeedGetFeedByIdIsAdminResponseDto getFeedByIdIsAdmin(Long id){
//        Feed feed = feedRepository.findById(id).orElseThrow(() -> new BadRequestException("존재하지 않는 feed_id"));
//        if (feed.getUser() ==null){
//            throw new BadRequestException("Feed 객체의 User 정보가 없습니다.");
//        }
//
//    }


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


}