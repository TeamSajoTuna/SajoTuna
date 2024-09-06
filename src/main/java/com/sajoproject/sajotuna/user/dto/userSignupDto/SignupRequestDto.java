package com.sajoproject.sajotuna.user.dto.userSignupDto;

import com.sajoproject.sajotuna.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    /**
     * @NotBlank : null 허용 x , 빈 문자열("") x, 공백(" ") x
     * @Email : 필드가 올바른 이메일 형식인지 검증
     * @Pattern : 입력할 패턴 지정
     */

    @NotBlank(message = "Nickname is mandatory")
    @Size(min=3, max=20, message = "Nickname must be between 3 and 20 characters")
    private String nickname;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+$", message=" 이메일 형식으로 입력하세요 ")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min=6, message = "Password must be at least 6 characters long")
    private String pw;

    private String userRole;
}
