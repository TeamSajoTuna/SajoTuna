package com.sajoproject.sajotuna.excption;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("유저를 찾을 수 없다.", HttpStatus.NOT_FOUND),
    FORBIDDEN("권한 없다.", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus httpStatus;
}
