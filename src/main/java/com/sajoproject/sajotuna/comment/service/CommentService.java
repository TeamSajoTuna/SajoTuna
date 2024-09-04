package com.sajoproject.sajotuna.comment.service;

import com.sajoproject.sajotuna.comment.dto.commentUpdateDto.CommentUpdateRequestDto;
import com.sajoproject.sajotuna.comment.dto.commentUpdateDto.CommentUpdateResponseDto;
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

    public CommentUpdateResponseDto commentUpdate(Long id, CommentUpdateRequestDto requestDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID의 댓글을 찾을 수 없습니다. : " + id));

        comment.update(requestDto);

        Comment updatedComment = commentRepository.save(comment);

        CommentUpdateResponseDto responseDto = new CommentUpdateResponseDto(
                updatedComment.getCommentId(),
                updatedComment.getContent(),
                updatedComment.getUser().getUserId(),
                updatedComment.getFeed().getFeedId()
        );
        return responseDto;
    }

    public void commentDelete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID의 댓글을 찾을 수 없습니다. : " + id));

        commentRepository.delete(comment);
    }
}
