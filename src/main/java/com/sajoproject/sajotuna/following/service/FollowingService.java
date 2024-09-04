package com.sajoproject.sajotuna.following.service;

import com.sajoproject.sajotuna.config.JwtUtil;
import com.sajoproject.sajotuna.exception.BadRequestException;
import com.sajoproject.sajotuna.following.dto.followDto.FollowDtoRequest;
import com.sajoproject.sajotuna.following.dto.followDto.FollowDtoResponse;
import com.sajoproject.sajotuna.following.entity.Follow;
import com.sajoproject.sajotuna.following.repository.FollowingRepository;
import com.sajoproject.sajotuna.user.dto.authUserDto.AuthUser;
import com.sajoproject.sajotuna.user.entity.User;
import com.sajoproject.sajotuna.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowingService {

    private final FollowingRepository followingRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    //팔로잉 추가
    @Transactional
    public FollowDtoResponse follow(FollowDtoRequest followDtoRequest, AuthUser authUser) {
        Long followingId = authUser.getId();

        // followedId가 널값일 경우
        if(followDtoRequest.getFollowedId()==null) {
            throw new BadRequestException("팔로우할 유저아이디를 입력해주세요.");
        }

        // 자기자신을 팔로우한 경우
        if(followingId.equals(followDtoRequest.getFollowedId())) {
            throw new BadRequestException("본인의 아이디를 입력하였습니다.");
        }

        // 데이터베이스에서 팔로잉할 유저 조회
        User followed = userRepository.findById(followDtoRequest.getFollowedId())
                .orElseThrow(() -> new BadRequestException("팔로우할 유저가 존재하지 않습니다."));

        User following = new User(followingId);

        // 이미 팔로우 한 사람을 다시 팔로우 할 경우
        if (followingRepository.existsByFollowingAndFollowed(following, followed)) {
            throw new BadRequestException("이미 팔로우 한 유저입니다.");
        }

        // 팔로우 관계 생성 및 저장
        Follow follow = new Follow(following, followed);
        followingRepository.save(follow);

        return new FollowDtoResponse(follow);
    }


    //팔로잉 삭제
    @Transactional
    public void unfollow(FollowDtoRequest followDtoRequest, HttpServletRequest request) {
        // JWT 토큰 헤더에서 추출
        String bearerToken = request.getHeader("Authorization");
        String jwt = jwtUtil.substringToken(bearerToken);

        // 토큰에서 Claim 추출
        Claims claims = jwtUtil.extractClaims(jwt);
        Long followingId = Long.valueOf(claims.get("sub", String.class));

        // followedId가 널값일 경우
        if (followDtoRequest.getFollowedId() == null) {
            throw new BadRequestException("팔로우를 삭제할 유저아이디를 입력해주세요.");
        }

        // 자기자신의 아이디를 넣은 경우
        if (followingId.equals(followDtoRequest.getFollowedId())) {
            throw new BadRequestException("본인의 아이디를 입력하였습니다.");
        }

        User following = new User(followingId);
        User followed = new User(followDtoRequest.getFollowedId());

        // 데이터베이스에서 Follow 객체 조회
        Follow follow = followingRepository.findByFollowingAndFollowed(following, followed)
                .orElseThrow(() -> new BadRequestException("팔로우 관계가 존재하지 않습니다."));

        followingRepository.delete(follow);
    }

}
