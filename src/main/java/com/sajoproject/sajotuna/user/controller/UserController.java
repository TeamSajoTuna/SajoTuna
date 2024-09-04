package com.sajoproject.sajotuna.user.controller;



import com.sajoproject.sajotuna.annotation.Auth;
import com.sajoproject.sajotuna.config.JwtUtil;
import com.sajoproject.sajotuna.exception.MethodArgumentNotValid;
import com.sajoproject.sajotuna.user.dto.TokenResponseDto;
import com.sajoproject.sajotuna.user.dto.authUserDto.AuthUser;
import com.sajoproject.sajotuna.user.dto.userDeleteDto.DeleteResponseDto;
import com.sajoproject.sajotuna.user.dto.userGetProfileDto.GetProfileResponseDto;
import com.sajoproject.sajotuna.user.dto.userSignInDto.SigninRequestDto;
import com.sajoproject.sajotuna.user.dto.userSignupDto.SignupRequestDto;
import com.sajoproject.sajotuna.user.dto.userUpdateProfileDto.UpdateRequestDto;
import com.sajoproject.sajotuna.user.dto.userUpdateProfileDto.UpdateResponseDto;
import com.sajoproject.sajotuna.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto) {
            String bearerToken = userService.signup(requestDto);
            return ResponseEntity.ok().header("Authorization", bearerToken).body(requestDto.toString());
    }

    @PostMapping("/users/signin")
    public ResponseEntity<Void> signIn(@RequestBody SigninRequestDto requestDto){
        TokenResponseDto tokenResponseDto = userService.signIn(requestDto);
        // Access Token(Authorization) 토큰 헤더에 추가
        return ResponseEntity.ok()
                .header("Authorization", tokenResponseDto.getAccessToken())
                .header("RefreshToken", tokenResponseDto.getRefreshToken())
                .build();
    }

    // 프로필 조회
    @GetMapping("/users/{userId}")
    public  ResponseEntity<GetProfileResponseDto> getProfile(@PathVariable Long userId, HttpServletRequest request) {
//        JWT 토큰 추출 , 현재 사용자 ID 추출
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            String currentUserId = jwtUtil.getUserId(token);

//           조회하는 프로필이  본인의 프로필인지 확인
            boolean isOwnProfile = currentUserId.equals(userId.toString());
//            프로필 정보 조회
            GetProfileResponseDto userProfile = userService.getProfile(userId, isOwnProfile);
            return ResponseEntity.ok(userProfile);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    // 프로필 수정
    @PutMapping("/users/{userId}")
    public ResponseEntity<UpdateResponseDto> updateProfile(@PathVariable Long userId, @RequestBody UpdateRequestDto updateRequestDto){
        return ResponseEntity.ok(userService.updateProfile(userId,updateRequestDto));
    }


    // 요청 헤더에서 JWT 토큰 추출하여 삭제
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<DeleteResponseDto> delete(@PathVariable Long userId, @Auth AuthUser user) {
        userService.deleteUser(userId, user);
        DeleteResponseDto responseDto = new DeleteResponseDto("정상 삭제 되었습니다.");
        return ResponseEntity.ok(responseDto);
    }

}
