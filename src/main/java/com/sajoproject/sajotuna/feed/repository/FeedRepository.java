package com.sajoproject.sajotuna.feed.repository;

import com.sajoproject.sajotuna.feed.dto.feedLikeDto.CountDto;
import com.sajoproject.sajotuna.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    //    List<Feed> findByUser_UserIdInOrderByModifiedAtDesc(List<Long> userIds);
    Page<Feed> findByUser_UserIdInOrderByModifiedAtDesc(List<Long> userIds, Pageable pageable);

// 인기게시글
    @Query("SELECT f FROM Feed f ORDER BY SIZE(f.likes) DESC")
    List<Feed> findTop10Feeds(Pageable pageable);


    // 게시물의 좋아요 수를 카운트
    @Query("SELECT new com.sajoproject.sajotuna.feed.dto.feedLikeDto.CountDto(f.feedId, COUNT(l.likesId)) FROM Feed f LEFT JOIN Likes l ON f.feedId = l.feed.feedId WHERE f.feedId = :feedId GROUP BY f.feedId")
    Optional<CountDto> countLikesByFeedId(@Param("feedId") Long feedId);
}
