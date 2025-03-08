package com.example.atipera.Exception;

import com.example.atipera.Models.ErrorResponse;

public class UserNotFoundException extends RuntimeException {
    private static ErrorResponse errorResponse = null;

    public UserNotFoundException(String message) {
        super(message);
        this.errorResponse = new ErrorResponse(404, message);
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
