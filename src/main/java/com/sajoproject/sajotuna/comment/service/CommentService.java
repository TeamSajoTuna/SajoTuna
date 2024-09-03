package com.sajoproject.sajotuna.comment.service;

import com.sajoproject.sajotuna.comment.dto.postCommentDto.PostCommentDtoRequest;
import com.sajoproject.sajotuna.comment.dto.postCommentDto.PostCommentDtoResponse;
import com.sajoproject.sajotuna.comment.entity.Comment;
import com.sajoproject.sajotuna.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public PostCommentDtoResponse postComment(PostCommentDtoRequest reqDto){

        Comment comment = new Comment(reqDto);
        PostCommentDtoResponse resDto =  new PostCommentDtoResponse(commentRepository.save(comment));
        return resDto;
    }
}
