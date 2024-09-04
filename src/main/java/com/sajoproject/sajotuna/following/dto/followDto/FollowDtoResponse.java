package com.sajoproject.sajotuna.following.dto.followDto;

import com.sajoproject.sajotuna.following.entity.Follow;
import lombok.Getter;

@Getter
public class FollowDtoResponse {

    private Long followingId;
    private Long followedId;

    public FollowDtoResponse(Follow follow) {
        this.followingId = follow.getFollowing().getUserId();
        this.followedId = follow.getFollowed().getUserId();
    }
}
