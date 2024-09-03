package com.sajoproject.sajotuna.excption;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SajoException extends RuntimeException {
    private final HttpStatus status;

    public SajoException(HttpStatus status, String message){
        super(message);
        this.status = status;
    }

}
