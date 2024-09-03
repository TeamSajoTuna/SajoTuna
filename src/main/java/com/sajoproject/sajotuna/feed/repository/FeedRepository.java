package com.sajoproject.sajotuna.feed.repository;

import com.sajoproject.sajotuna.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {
}
