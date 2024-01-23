package com.backend.exception.CustomExceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException{
    public NotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}
