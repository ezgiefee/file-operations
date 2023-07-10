package com.motus.fileoperations.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FileExceptionHandler {

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<String> handleFileSizeLimitExceededException(FileSizeLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
