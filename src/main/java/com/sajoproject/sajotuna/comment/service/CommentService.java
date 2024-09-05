package com.sajoproject.sajotuna.comment.service;

import com.sajoproject.sajotuna.comment.dto.commentUpdateDto.CommentUpdateRequestDto;
import com.sajoproject.sajotuna.comment.dto.commentUpdateDto.CommentUpdateResponseDto;
import com.sajoproject.sajotuna.comment.dto.getCommentFromFeedDto.GetCommentFromFeedDtoRequest;
import com.sajoproject.sajotuna.comment.dto.getCommentFromFeedDto.GetCommentFromFeedDtoResponse;
import com.sajoproject.sajotuna.comment.dto.postCommentDto.PostCommentDtoRequest;
import com.sajoproject.sajotuna.comment.dto.postCommentDto.PostCommentDtoResponse;
import com.sajoproject.sajotuna.comment.entity.Comment;
import com.sajoproject.sajotuna.comment.repository.CommentRepository;
import com.sajoproject.sajotuna.user.entity.User;
import com.sajoproject.sajotuna.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostCommentDtoResponse postComment(PostCommentDtoRequest reqDto){

        Comment comment = new Comment(reqDto);
        PostCommentDtoResponse resDto =  new PostCommentDtoResponse(commentRepository.save(comment));
        return resDto;
    }

    public  List<GetCommentFromFeedDtoResponse> getCommentFromFeed(GetCommentFromFeedDtoRequest reqDto){
        List<Comment> commentList = commentRepository.findByFeed_FeedId(reqDto.getFeedId());

        List<GetCommentFromFeedDtoResponse> resDto = commentList.stream()
                .map(GetCommentFromFeedDtoResponse::new)
                .collect(Collectors.toList());
        return resDto;

    }

    // 댓글 수정
    @Transactional
    public CommentUpdateResponseDto commentUpdate(
            Long id, CommentUpdateRequestDto requestDto, Long currentUserId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID의 댓글을 찾을 수 없습니다. : " + id));

        if (!comment.getUser().getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
        }

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

    // 댓글 삭제
    @Transactional
    public void commentDelete(Long id, Long currentUserId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID의 댓글을 찾을 수 없습니다. : " + id));

        if (!comment.getUser().getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
