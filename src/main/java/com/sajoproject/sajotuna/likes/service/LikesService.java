package com.sajoproject.sajotuna.likes.service;

import com.sajoproject.sajotuna.exception.BadRequestException;
import com.sajoproject.sajotuna.feed.entity.Feed;
import com.sajoproject.sajotuna.feed.repository.FeedRepository;
import com.sajoproject.sajotuna.likes.dto.createLikesDto.CreateLikesDtoRequest;
import com.sajoproject.sajotuna.likes.dto.createLikesDto.CreateLikesDtoResponse;
import com.sajoproject.sajotuna.likes.dto.deleteLikesDto.DeleteLikesDtoRequest;
import com.sajoproject.sajotuna.likes.entity.Likes;
import com.sajoproject.sajotuna.likes.repository.LikesRepository;
import com.sajoproject.sajotuna.user.dto.authUserDto.AuthUser;
import com.sajoproject.sajotuna.user.entity.User;
import com.sajoproject.sajotuna.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    // 좋아요 추가
    @Transactional
    public CreateLikesDtoResponse createLikes(CreateLikesDtoRequest request, AuthUser authUser) {
        Long userId = authUser.getId();

        // feedId가 널값일 경우
        if(ObjectUtils.isEmpty(request.getFeedId())) {
            throw new BadRequestException("좋아요를 추가할 피드아이디를 넣어주세요.");
        }
        Long feedId = request.getFeedId();

        // 데이터베이스에서 feed 와 user 를 조회
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new BadRequestException("피드를 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("사용자를 찾을 수 없습니다."));

        // 본인이 작성한 게시물에 좋아요를 남길 경우
        Long feedUserId = feed.getUser().getUserId();
        if(Objects.equals(userId, feedUserId)) {
            throw new BadRequestException("본인이 작성한 게시물에 좋아요를 남길 수 없습니다.");
        }

        // 좋아요 관계 생성 및 저장
        Likes likes = new Likes(user, feed);

        // 한 사용자가 같은 게시물에 좋아요를 누른 경우
        if(likesRepository.existsByUserAndFeed(user,feed)) {
            throw new BadRequestException("한 게시물에는 사용자당 한 번만 좋아요가 가능합니다.");
        }

        return new CreateLikesDtoResponse(likesRepository.save(likes));
    }


    // 좋아요 삭제
    public void deleteLikes(DeleteLikesDtoRequest request, AuthUser authUser) {
        Long userId = authUser.getId();

        // feedId가 널값일 경우
        if(ObjectUtils.isEmpty(request.getFeedId())) {
            throw new BadRequestException("좋아요를 삭제할 피드아이디를 넣어주세요.");
        }

        Long feedId = request.getFeedId();
        Feed feed = new Feed(feedId);
        User user = new User(userId);

        //데이터베이스에서 likes 객체 조회
        Likes likes = likesRepository.findByFeedAndUser(feed, user)
                .orElseThrow(()-> new BadRequestException ("이 게시물에 좋아요를 하지 않아 삭제할 수 없습니다.") );

        likesRepository.delete(likes);
    }

}
