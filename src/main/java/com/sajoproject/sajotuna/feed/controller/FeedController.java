package com.sajoproject.sajotuna.feed.controller;

import com.sajoproject.sajotuna.feed.dto.feedDeleteDto.FeedDeleteResponseDto;
import com.sajoproject.sajotuna.feed.dto.feedUpdatdDto.FeedUpdateRequestDto;
import com.sajoproject.sajotuna.feed.dto.feedUpdatdDto.FeedUpdateResponseDto;
import com.sajoproject.sajotuna.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

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
