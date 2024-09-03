package com.sajoproject.sajotuna.comment.repository;

import com.sajoproject.sajotuna.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
