package com.sajoproject.sajotuna.feed.repository;

import com.sajoproject.sajotuna.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
//    List<Feed> findByUser_UserIdInOrderByModifiedAtDesc(List<Long> userIds);
Page<Feed> findByUser_UserIdInOrderByModifiedAtDesc(List<Long> userIds, Pageable pageable);

    List<Feed> findByUserId(Long userId);
    boolean existsByUserId(Long userId);

}
