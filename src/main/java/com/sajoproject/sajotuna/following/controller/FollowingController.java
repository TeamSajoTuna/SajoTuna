package com.sajoproject.sajotuna.following.controller;

import com.sajoproject.sajotuna.annotation.Auth;
import com.sajoproject.sajotuna.following.dto.followDto.FollowDtoRequest;
import com.sajoproject.sajotuna.following.dto.followDto.FollowDtoResponse;
import com.sajoproject.sajotuna.following.dto.unfollowDto.UnfollowDtoResponse;
import com.sajoproject.sajotuna.following.service.FollowingService;
import com.sajoproject.sajotuna.user.dto.authUserDto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/following")
@RequiredArgsConstructor
public class FollowingController {

    private final FollowingService followingService;

    //팔로잉 추가
    @PostMapping
    public ResponseEntity<FollowDtoResponse> follow (@RequestBody FollowDtoRequest request, @Auth AuthUser authUser) {
        return ResponseEntity.ok(followingService.follow(request, authUser));
    }

    //팔로잉 삭제
    @DeleteMapping
    public ResponseEntity<UnfollowDtoResponse> unfollow (@RequestBody FollowDtoRequest request, @Auth AuthUser authUser) {
        followingService.unfollow(request,authUser);
        UnfollowDtoResponse responseDto = new UnfollowDtoResponse("팔로잉이 정상 삭제 되었습니다.");
        return ResponseEntity.ok(responseDto);
    }

}
