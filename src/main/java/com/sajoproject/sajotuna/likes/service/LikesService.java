package com.sajoproject.sajotuna.likes.service;

import com.sajoproject.sajotuna.exception.BadRequestException;
import com.sajoproject.sajotuna.feed.entity.Feed;
import com.sajoproject.sajotuna.feed.repository.FeedRepository;
import com.sajoproject.sajotuna.likes.dto.createLikesDto.CreateLikesDtoRequest;
import com.sajoproject.sajotuna.likes.dto.createLikesDto.CreateLikesDtoResponse;
import com.sajoproject.sajotuna.likes.entity.Likes;
import com.sajoproject.sajotuna.likes.repository.LikesRepository;
import com.sajoproject.sajotuna.user.dto.authUserDto.AuthUser;
import com.sajoproject.sajotuna.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final FeedRepository feedRepository;

    // 좋아요 추가
    @Transactional
    public CreateLikesDtoResponse createLikes(CreateLikesDtoRequest request, AuthUser authUser) {
        Long userId = authUser.getId();

        // feedId가 널값일 경우
        if(request.getFeedId()==null) {
            throw new BadRequestException("좋아요를 추가할 피드아이디를 넣어주세요.");
        }

        // feedRepository 에 피드가 있는지 조회
        if(!feedRepository.existsById(request.getFeedId())) {
            throw new BadRequestException("좋아요할 피드가 존재하지 않습니다.");
        }
        Long feedId = request.getFeedId();

        Feed feed = new Feed(feedId);
        User user = new User(userId);

        // 본인이 작성한 게시물에 좋아요를 남길 경우 - 수정필요
//        Long feedUserId = feed.getUser().getUserId();
//        if(Objects.equals(userId, feedUserId)) {
//            throw new BadRequestException("본인이 작성한 게시물에 좋아요를 남길 수 없습니다.");
//        }

        // 좋아요 관계 생성 및 저장
        Likes likes = new Likes(user, feed);

        // 한 사용자가 같은 게시물에 좋아요를 누른 경우
        if(likesRepository.existsByUserAndFeed(user,feed)) {
            throw new BadRequestException("같은 게시물에는 사용자당 한 번만 좋아요가 가능합니다.");
        }

        likesRepository.save(likes);

        return new CreateLikesDtoResponse(likes);

    }




}
