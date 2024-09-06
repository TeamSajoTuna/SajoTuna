package com.sajoproject.sajotuna.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> BadRequestException(BadRequestException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> ConflictException(ConflictException ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> ForbiddenException(ForbiddenException ex) {
        return ResponseEntity.status(403).body(ex.getMessage());
    }

    @ExceptionHandler(MethodNotAllowed.class)
    public ResponseEntity<String> ForbiddenException(MethodNotAllowed ex) {
        return ResponseEntity.status(405).body(ex.getMessage());
    }


    // Json 형태
    @ExceptionHandler(Forbidden.class)
    public ResponseEntity<Map<String, Object>> Forbidden(Forbidden ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> NotFound(UserNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(UnAuthorized.class)
    public ResponseEntity<Map<String, Object>> UnAuthorized(UnAuthorized ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(Conflict.class)
    public ResponseEntity<Map<String, Object>> Conflict(Conflict ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        return getErrorResponse(status, ex.getMessage());
    }


    public ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.name());
        errorResponse.put("code", status.value());
        errorResponse.put("message", message);

        return new ResponseEntity<>(errorResponse, status);
    }

    // 유효성 검증
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> MethodArgumentNotValid(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getErrorResponse(status, ex.getMessage());
    }

}
