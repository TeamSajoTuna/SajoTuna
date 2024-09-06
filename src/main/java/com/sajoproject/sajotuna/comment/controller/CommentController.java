package com.sajoproject.sajotuna.comment.controller;

import com.sajoproject.sajotuna.annotation.Auth;
import com.sajoproject.sajotuna.comment.dto.commentDeleteDto.CommentDeleteResponseDto;
import com.sajoproject.sajotuna.comment.dto.commentUpdateDto.CommentUpdateRequestDto;
import com.sajoproject.sajotuna.comment.dto.commentUpdateDto.CommentUpdateResponseDto;
import com.sajoproject.sajotuna.comment.dto.getCommentFromFeedDto.GetCommentFromFeedDtoRequest;
import com.sajoproject.sajotuna.comment.dto.getCommentFromFeedDto.GetCommentFromFeedDtoResponse;
import com.sajoproject.sajotuna.comment.dto.postCommentDto.PostCommentDtoRequest;
import com.sajoproject.sajotuna.comment.dto.postCommentDto.PostCommentDtoResponse;
import com.sajoproject.sajotuna.comment.service.CommentService;
import com.sajoproject.sajotuna.user.dto.authUserDto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
/*  댓글 생성 : content, feedId 필요
    대댓글 생성 : content, feedId, parentCommentId 필요.
    parentCommentId에는 commentId(대댓글을 작성할 댓글)을 입력. */
    @PostMapping
    public ResponseEntity<PostCommentDtoResponse> postComment(
            @RequestBody PostCommentDtoRequest reqDto, @Auth AuthUser authUser){
        reqDto.setUserId(authUser.getId());
        PostCommentDtoResponse resDto = commentService.postComment(reqDto);
        return ResponseEntity.ok().body(resDto);
    }

    @GetMapping
    public ResponseEntity<List<GetCommentFromFeedDtoResponse>> getCommentFromFeed(
            @RequestParam(required = true, defaultValue = "0") Long feedId) {
        System.out.println("reqDto.getFeedId() = " + feedId);
        List<GetCommentFromFeedDtoResponse> resDto = commentService.getCommentFromFeed(feedId);
        return ResponseEntity.ok().body(resDto);
    }

    // 댓글 수정 id=commentId
    @PutMapping("/{id}")
    public ResponseEntity<CommentUpdateResponseDto> commentUpdate(
            @PathVariable Long id,
            @RequestBody CommentUpdateRequestDto requestDto,
            @Auth AuthUser authUser){
        CommentUpdateResponseDto responseDto = commentService.commentUpdate(
                id, requestDto, authUser.getId());
        return ResponseEntity.ok(responseDto);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<CommentDeleteResponseDto> commentDelete(
            @PathVariable  Long id,
            @Auth AuthUser authUser) {
        commentService.commentDelete(id, authUser.getId());
        CommentDeleteResponseDto responseDto = new CommentDeleteResponseDto("댓글이 삭제되었습니다.");
        return ResponseEntity.ok(responseDto);
    }
}
