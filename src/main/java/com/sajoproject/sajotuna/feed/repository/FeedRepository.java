package com.sajoproject.sajotuna.feed.repository;

import com.sajoproject.sajotuna.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
//    List<Feed> findByUser_UserIdInOrderByModifiedAtDesc(List<Long> userIds);
Page<Feed> findByUser_UserIdInOrderByModifiedAtDesc(List<Long> userIds, Pageable pageable);

}
