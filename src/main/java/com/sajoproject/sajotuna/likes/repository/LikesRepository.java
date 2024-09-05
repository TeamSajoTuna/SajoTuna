package com.sajoproject.sajotuna.likes.repository;

import com.sajoproject.sajotuna.feed.entity.Feed;
import com.sajoproject.sajotuna.likes.entity.Likes;
import com.sajoproject.sajotuna.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    boolean existsByUserAndFeed(User user, Feed feed);
    Optional<Likes> findByFeedAndUser(Feed feed, User user);

}
