package com.sajoproject.sajotuna.feed.controller;

import com.sajoproject.sajotuna.feed.dto.feedGetFeedByIdDto.FeedGetFeedByIdDtoResponse;
import com.sajoproject.sajotuna.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sajoproject.sajotuna.feed.dto.createFeedDto.CreateFeedRequestDto;
import com.sajoproject.sajotuna.feed.dto.createFeedDto.CreateFeedResponseDto;
import com.sajoproject.sajotuna.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;


    //게시물 생성 - request로 title, content, userId 필요
    @PostMapping("/feed")
    public ResponseEntity<CreateFeedResponseDto> createFeed(@RequestBody CreateFeedRequestDto requestDto) {
        return ResponseEntity.ok(feedService.createFeed(requestDto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<FeedGetFeedByIdDtoResponse> getFeedById(@PathVariable Long id) {

        FeedGetFeedByIdDtoResponse resFeed = feedService.getFeedById(id);
        return ResponseEntity.ok().body(resFeed);
    }

    @GetMapping("/paging")
    public void getFeedPaging(
            @RequestParam(required = false, defaultValue = "0") int page,  // 기본 페이지 번호는 0
            @RequestParam(required = false, defaultValue = "10") int size  // 기본 페이지 크기는 10
    ) {


    }


}


