package com.fleencorp.detectivez.model.response;

public class ErrorResponse {
    private final String code;
    private final String message;
    private final int status;

    public ErrorResponse(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
