package com.sajoproject.sajotuna.feed.controller;

import com.sajoproject.sajotuna.feed.dto.createFeedDto.CreateFeedRequestDto;
import com.sajoproject.sajotuna.feed.dto.createFeedDto.CreateFeedResponseDto;
import com.sajoproject.sajotuna.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedController {

  private final FeedService feedService;

  //게시물 생성 - request로 title, content, userId 필요
  @PostMapping("/feed")
  public ResponseEntity<CreateFeedResponseDto> createFeed(@RequestBody CreateFeedRequestDto requestDto) {
    return ResponseEntity.ok(feedService.createFeed(requestDto));
  }


}
