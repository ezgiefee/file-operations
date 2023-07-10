package com.motus.fileoperations.exception;

public class FileSizeLimitExceededException extends RuntimeException{
    public FileSizeLimitExceededException(String message){
        super(message);
    }
}
