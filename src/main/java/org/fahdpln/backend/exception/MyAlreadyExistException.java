package org.fahdpln.backend.exception;

import org.fahdpln.backend.utils.MyErrorResponse;

public class MyAlreadyExistException extends RuntimeException {
    private MyErrorResponse response;

    public MyAlreadyExistException(String message) {
        super(message);
    }

    public MyAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyAlreadyExistException(MyErrorResponse response) {
        super(response.getMessage());
        this.response = response;
    }

    public MyErrorResponse getResponse() {
        return response;
    }
    
}
