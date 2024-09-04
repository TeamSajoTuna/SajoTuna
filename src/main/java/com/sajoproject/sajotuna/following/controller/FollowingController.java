package com.sajoproject.sajotuna.following.controller;

import com.sajoproject.sajotuna.following.dto.followDto.FollowDtoRequest;
import com.sajoproject.sajotuna.following.dto.followDto.FollowDtoResponse;
import com.sajoproject.sajotuna.following.service.FollowingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowingController {

    private final FollowingService followingService;

    //팔로잉 추가
    @PostMapping
    public ResponseEntity<FollowDtoResponse> follow (@RequestBody FollowDtoRequest followDtoRequest, HttpServletRequest request) {
        return ResponseEntity.ok(followingService.follow(followDtoRequest, request));
    }

    //팔로잉 삭제
    //unfollow
}
