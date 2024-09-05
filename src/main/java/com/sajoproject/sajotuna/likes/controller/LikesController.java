package com.sajoproject.sajotuna.likes.controller;

import com.sajoproject.sajotuna.annotation.Auth;
import com.sajoproject.sajotuna.likes.dto.createLikesDto.CreateLikesDtoRequest;
import com.sajoproject.sajotuna.likes.dto.createLikesDto.CreateLikesDtoResponse;
import com.sajoproject.sajotuna.likes.dto.deleteLikesDto.DeleteLikesDtoRequest;
import com.sajoproject.sajotuna.likes.dto.deleteLikesDto.DeleteLikesDtoResponse;
import com.sajoproject.sajotuna.likes.service.LikesService;
import com.sajoproject.sajotuna.user.dto.authUserDto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikesController {

    private final LikesService likesService;

    // 좋아요 추가
    @PostMapping
    public ResponseEntity<CreateLikesDtoResponse>  createLikes (@RequestBody CreateLikesDtoRequest request, @Auth AuthUser authUser) {
        return ResponseEntity.ok(likesService.createLikes(request, authUser));
    }

    //좋아요 삭제
    @DeleteMapping
    public ResponseEntity<DeleteLikesDtoResponse> deleteLikes (@RequestBody DeleteLikesDtoRequest request, @Auth AuthUser authUser ) {
        likesService.deleteLikes(request,authUser);
        DeleteLikesDtoResponse response = new DeleteLikesDtoResponse("좋아요가 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }


}
