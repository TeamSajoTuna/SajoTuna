package com.sajoproject.sajotuna.user.controller;

import com.sajoproject.sajotuna.user.dto.userDeleteDto.DeleteResponseDto;
import com.sajoproject.sajotuna.user.dto.userSignInDto.SigninRequestDto;
import com.sajoproject.sajotuna.user.dto.userSignupDto.SignupRequestDto;
import com.sajoproject.sajotuna.user.dto.userSignupDto.SignupResponseDto;
import com.sajoproject.sajotuna.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto requestDto) {
        return ResponseEntity.ok(userService.signup(requestDto));
    }

    @PostMapping("/users/signin")
    public ResponseEntity<Void> signIn(@RequestBody SigninRequestDto requestDto, HttpServletResponse response){
        String bearerToken = userService.signIn(requestDto);
        // Access Token(Authorization) 토큰 헤더에 추가
        return ResponseEntity.ok().header("Authorization", bearerToken).build();
    }

    // 요청 헤더에서 JWT 토큰 추출하여 삭제
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<DeleteResponseDto> delete(@PathVariable Long userId, HttpServletRequest request) {
        userService.deleteUser(userId, request);
        DeleteResponseDto responseDto = new DeleteResponseDto("정상 삭제 되었습니다.");
        return ResponseEntity.ok(responseDto);
    }

}
