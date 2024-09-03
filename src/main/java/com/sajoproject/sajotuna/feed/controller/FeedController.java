package com.sajoproject.sajotuna.feed.controller;

import com.sajoproject.sajotuna.feed.dto.feedGetFeedByIdDto.FeedGetFeedByIdDtoResponse;
import com.sajoproject.sajotuna.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

  private final FeedService feedService;

  @GetMapping("/{id}")
  public ResponseEntity<FeedGetFeedByIdDtoResponse> getFeedById(@PathVariable Long id){

    FeedGetFeedByIdDtoResponse resFeed = feedService.getFeedById(id);
    return ResponseEntity.ok().body(resFeed);

  }

  @GetMapping("/paging")
  public void getFeedPaging(
          @RequestParam(required = false, defaultValue = "0") int page,  // 기본 페이지 번호는 0
          @RequestParam(required = false, defaultValue = "10") int size  // 기본 페이지 크기는 10
  ){


  }


}
