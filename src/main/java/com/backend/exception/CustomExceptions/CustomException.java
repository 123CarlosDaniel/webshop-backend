package com.backend.exception.CustomExceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException{
    private final HttpStatus httpStatus;
    public CustomException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }
}
