package com.backend.exception;

import com.backend.exception.CustomExceptions.CustomException;
import com.backend.response.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(er -> {
            errorMap.put(er.getField(), er.getDefaultMessage());
        });
        return errorMap;
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleNotFoundUser(CustomException ex) {
        return new ResponseEntity<>(new ResponseMessage(ex.getMessage()), ex.getHttpStatus());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException ex) {
        return new ResponseEntity<>(new ResponseMessage("The requested file does not exist"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException ex) {
        return new ResponseEntity<>(new ResponseMessage("Error while processing the file"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
