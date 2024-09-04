package com.sajoproject.sajotuna.feed.controller;

import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoRequest;
import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoResponse;
import com.sajoproject.sajotuna.feed.dto.feedDeleteDto.FeedDeleteResponseDto;
import com.sajoproject.sajotuna.feed.dto.feedGetFeedByIdDto.FeedGetFeedByIdDtoResponse;
import com.sajoproject.sajotuna.feed.dto.feedPagingDto.FeedPagingDtoResponse;
import com.sajoproject.sajotuna.feed.dto.feedUpdatdDto.FeedUpdateRequestDto;
import com.sajoproject.sajotuna.feed.dto.feedUpdatdDto.FeedUpdateResponseDto;
import com.sajoproject.sajotuna.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestParam Long id
    ) {
        Page<FeedPagingDtoResponse> pagingFeed = feedService.getFeedPaging(page,size, id);
        return pagingFeed;


    }

    //게시물 생성 - request로 title, content, userId 필요
    @PostMapping
    public ResponseEntity<FeedCreateDtoResponse> feedCreate(@RequestBody FeedCreateDtoRequest requestDto) {
        return ResponseEntity.ok(feedService.feedCreate(requestDto));
    }

    // 게시물 수정
    @PutMapping("/feed/{id}")
    public ResponseEntity<FeedUpdateResponseDto> feedUpdate(
            @PathVariable Long id, @RequestBody FeedUpdateRequestDto requestDto) {
        FeedUpdateResponseDto responseDto = feedService.feedUpdate(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 게시물 삭제
    @DeleteMapping("/feed/{id}")
    public ResponseEntity<FeedDeleteResponseDto> feedDelete(@PathVariable Long id) {
        feedService.feedDelete(id);
        FeedDeleteResponseDto responseDto = new FeedDeleteResponseDto("삭제 되었습니다.");
        return ResponseEntity.ok(responseDto);

    }
}


