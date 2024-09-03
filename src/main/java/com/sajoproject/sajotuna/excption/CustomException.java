package com.sajoproject.sajotuna.excption;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CustomException extends RuntimeException {

    public CustomException(ErrorCode errorCode){
        super(errorCode.getMessage());
    }


}
