package org.fahdpln.backend.exception;

import org.fahdpln.backend.utils.MyErrorResponse;

public class MyAuthException extends RuntimeException {
    private MyErrorResponse response;

    public MyAuthException(String message) {
        super(message);
    }

    public MyAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyAuthException(MyErrorResponse response) {
        super(response.getMessage());
        this.response = response;
    }

    public MyErrorResponse getResponse() {
        return response;
    }
    
}
