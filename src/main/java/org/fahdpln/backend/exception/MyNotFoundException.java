package org.fahdpln.backend.exception;

import org.fahdpln.backend.utils.MyErrorResponse;

public class MyNotFoundException extends RuntimeException {
    private MyErrorResponse response;

    public MyNotFoundException(String message) {
        super(message);
    }

    public MyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyNotFoundException(MyErrorResponse response) {
        super(response.getMessage());
        this.response = response;
    }

    public MyErrorResponse getResponse() {
        return response;
    }
    
}
