package com.fleencorp.detectivez.exception;

public class AppException extends RuntimeException{
    private final String errorCode;

    public AppException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
