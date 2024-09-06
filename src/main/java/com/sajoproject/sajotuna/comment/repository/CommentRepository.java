package com.sajoproject.sajotuna.comment.repository;

import com.sajoproject.sajotuna.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByFeed_FeedId(Long id);
    List<Comment> findAllByReplyComment(Comment comment);
}
