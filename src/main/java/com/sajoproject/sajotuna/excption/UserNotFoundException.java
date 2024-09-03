package com.sajoproject.sajotuna.excption;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends SajoException {

    private static final String message = "User not found";
    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public UserNotFoundException() {
        super(httpStatus, message);
    }
}
