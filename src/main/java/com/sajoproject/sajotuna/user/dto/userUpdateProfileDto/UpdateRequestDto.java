package com.sajoproject.sajotuna.user.dto.userUpdateProfileDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@Getter


public class UpdateRequestDto {

    private Long userId;
    private String currentPassword;
    private String nickname;
    private String email;

    /*
     * 패스워드 형식
     * - 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함합니다.
     * - 비밀번호는 최소 8글자 이상이어야 합니다.
     */
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
            message = "패스워드는 영문 , 숫자, 특수문자를 최소 1글자씩 포함해야 합니다."
    )
    private String newPassword;

}
