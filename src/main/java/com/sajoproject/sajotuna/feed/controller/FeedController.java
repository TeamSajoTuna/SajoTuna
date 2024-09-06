package com.sajoproject.sajotuna.feed.controller;

import com.sajoproject.sajotuna.annotation.Auth;
import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoRequest;
import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoResponse;
import com.sajoproject.sajotuna.feed.dto.feedDeleteDto.FeedDeleteResponseDto;
import com.sajoproject.sajotuna.feed.dto.feedGetFeedByIdDto.FeedGetFeedByIdDtoResponse;
import com.sajoproject.sajotuna.feed.dto.feedPagingDto.FeedPagingDtoResponse;
import com.sajoproject.sajotuna.feed.dto.feedUpdatdDto.FeedUpdateRequestDto;
import com.sajoproject.sajotuna.feed.dto.feedUpdatdDto.FeedUpdateResponseDto;
import com.sajoproject.sajotuna.feed.service.FeedService;
import com.sajoproject.sajotuna.user.dto.authUserDto.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;


    @GetMapping("/{id}")
    public ResponseEntity<FeedGetFeedByIdDtoResponse> getFeedById(@PathVariable Long id) {

        FeedGetFeedByIdDtoResponse resFeed = feedService.getFeedById(id);
        return ResponseEntity.ok().body(resFeed);
    }

    @GetMapping("/paging")
    public Page<FeedPagingDtoResponse> getFeedPaging(
            @RequestParam(required = false, defaultValue = "0") int page,  // 기본 페이지 번호는 0
            @RequestParam(required = false, defaultValue = "10") int size,  // 기본 페이지 크기는 10
            @Auth AuthUser authUser
            ) {
        Page<FeedPagingDtoResponse> pagingFeed = feedService.getFeedPaging(page,size, authUser.getId());

        return pagingFeed;


    }

    //게시물 생성 - request로 title, content 필요
    @PostMapping
    public ResponseEntity<FeedCreateDtoResponse> feedCreate(
            @RequestBody FeedCreateDtoRequest requestDto,
            @Auth AuthUser authUser) {
        FeedCreateDtoResponse responseDto = feedService.feedCreate(requestDto,authUser.getId());
        return ResponseEntity.ok(responseDto);
    }

    // 게시물 수정 - request로 title, content 필요
    @PutMapping("/{id}")
    public ResponseEntity<FeedUpdateResponseDto> feedUpdate(
            @PathVariable Long id,
            @RequestBody FeedUpdateRequestDto requestDto,
            @Auth AuthUser authUser) {
        FeedUpdateResponseDto responseDto = feedService.feedUpdate(id, requestDto, authUser.getId());
        return ResponseEntity.ok(responseDto);
    }

    // 게시물 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedDeleteResponseDto> feedDelete(
            @PathVariable Long id,
            @Auth AuthUser authUser) {

        feedService.feedDelete(id, authUser.getId());

        FeedDeleteResponseDto responseDto = new FeedDeleteResponseDto("삭제 되었습니다.");
        return ResponseEntity.ok(responseDto);

    }

    //    인기 게시물
    @GetMapping("/top-feeds")
    public ResponseEntity<List<FeedPagingDtoResponse>> getTop10Feeds() {
        List<FeedPagingDtoResponse> top10Feeds = feedService.getTop10Feeds();
        return ResponseEntity.ok(top10Feeds);
    }

    // 한 게시물의 총 좋아요 개수 카운트
    @GetMapping("/likescount")
    public ResponseEntity<FeedLikeCountResponseDto> getLikeCountByFeedId(@RequestBody FeedLikeCountRequestDto requestDto) {
    FeedLikeCountResponseDto feedLikeCountDto = feedService.getLikeCountByFeedId(requestDto.getFeedId());
    return ResponseEntity.ok(feedLikeCountDto);
    }
}