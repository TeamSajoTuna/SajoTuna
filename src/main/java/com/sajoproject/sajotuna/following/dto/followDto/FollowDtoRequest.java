package com.sajoproject.sajotuna.following.dto.followDto;

import lombok.Getter;

@Getter
public class FollowDtoRequest {
    // 팔로우 받을 사람
    private Long followedId;
}
