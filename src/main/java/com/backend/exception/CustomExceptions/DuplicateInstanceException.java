package com.backend.exception.CustomExceptions;

import org.springframework.http.HttpStatus;

public class DuplicateInstanceException extends CustomException{
    public DuplicateInstanceException(String message, HttpStatus status) {
        super(message, status);
    }
}
