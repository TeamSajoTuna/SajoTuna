package com.sajoproject.sajotuna.comment.controller;

import com.sajoproject.sajotuna.comment.dto.getCommentFromFeedDto.GetCommentFromFeedDtoRequest;
import com.sajoproject.sajotuna.comment.dto.getCommentFromFeedDto.GetCommentFromFeedDtoResponse;
import com.sajoproject.sajotuna.comment.dto.commentDeleteDto.CommentDeleteResponseDto;
import com.sajoproject.sajotuna.comment.dto.commentUpdateDto.CommentUpdateRequestDto;
import com.sajoproject.sajotuna.comment.dto.commentUpdateDto.CommentUpdateResponseDto;
import com.sajoproject.sajotuna.comment.dto.postCommentDto.PostCommentDtoRequest;
import com.sajoproject.sajotuna.comment.dto.postCommentDto.PostCommentDtoResponse;
import com.sajoproject.sajotuna.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<PostCommentDtoResponse> postComment(@RequestBody PostCommentDtoRequest reqDto){
        System.out.println("reqDto = "+ reqDto);

        PostCommentDtoResponse resDto = commentService.postComment(reqDto);
        return ResponseEntity.ok().body(resDto);
    }

    @GetMapping
    public ResponseEntity<List<GetCommentFromFeedDtoResponse>> getCommentFromFeed(@RequestBody GetCommentFromFeedDtoRequest reqDto){
        System.out.println("reqDto.getFeedId() = " + reqDto.getFeedId());
        List<GetCommentFromFeedDtoResponse> resDto = commentService.getCommentFromFeed(reqDto);
        return ResponseEntity.ok().body(resDto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentUpdateResponseDto> commentUpdate(
            @PathVariable Long id,
            @RequestBody CommentUpdateRequestDto requestDto, HttpServletRequest request){
        CommentUpdateResponseDto responseDto = commentService.commentUpdate(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommentDeleteResponseDto> commentDelete(@PathVariable  Long id) {
        commentService.commentDelete(id);
        CommentDeleteResponseDto responseDto = new CommentDeleteResponseDto("댓글이 삭제되었습니다.");
        return ResponseEntity.ok(responseDto);
    }
}
