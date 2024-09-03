package org.fahdpln.backend.exception;

import org.fahdpln.backend.utils.MyErrorResponse;

public class MyNotAcceptableException extends RuntimeException {
    private MyErrorResponse response;

    public MyNotAcceptableException(String message) {
        super(message);
    }

    public MyNotAcceptableException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyNotAcceptableException(MyErrorResponse response) {
        super(response.getMessage());
        this.response = response;
    }

    public MyErrorResponse getResponse() {
        return response;
    }
    
}
