package com.sajoproject.sajotuna.user.controller;


import com.sajoproject.sajotuna.user.dto.userDeleteDto.DeleteResponseDto;
import com.sajoproject.sajotuna.user.dto.userGetProfileDto.GetProfileResponseDto;
import com.sajoproject.sajotuna.user.dto.userSignInDto.SigninRequestDto;
import com.sajoproject.sajotuna.user.dto.userSignupDto.SignupRequestDto;
import com.sajoproject.sajotuna.user.dto.userSignupDto.SignupResponseDto;
import com.sajoproject.sajotuna.user.dto.userUpdateProfileDto.UpdateRequestDto;
import com.sajoproject.sajotuna.user.dto.userUpdateProfileDto.UpdateResponseDto;
import com.sajoproject.sajotuna.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.bind.annotation.RestController;


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
        response.setHeader("Authorization", bearerToken);
        return ResponseEntity.ok().build();
    }

    // 프로필 조회
    @GetMapping("/users/{userId}")
    public  ResponseEntity<GetProfileResponseDto> getProfile(@PathVariable Long userId){
        GetProfileResponseDto userProfile = userService.getProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    // 프로필 수정
    @PutMapping("/users/{userId}")
    public ResponseEntity<UpdateResponseDto> updateProfile(@PathVariable Long userId, @RequestBody UpdateRequestDto updateRequestDto){
        return ResponseEntity.ok(userService.updateProfile(userId,updateRequestDto));
    }

    // 요청 헤더에서 JWT 토큰 추출하여 삭제
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<DeleteResponseDto> delete(@PathVariable Long userId, HttpServletRequest request) {
        userService.deleteUser(userId, request);
        DeleteResponseDto responseDto = new DeleteResponseDto("정상 삭제 되었습니다.");
        return ResponseEntity.ok(responseDto);
    }

}
