package com.fleencorp.detectivez.exception;

public class FailedOperationException extends AppException{
    public FailedOperationException() {
        super("Operation failed, try again", "OP_FAILED");
    }
}
