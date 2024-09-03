package org.fahdpln.backend.exception; // Update the package name as per your project structure

import java.util.List;
import java.util.stream.Collectors;

import org.fahdpln.backend.utils.MyError;
import org.fahdpln.backend.utils.MyErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle NotAcceptableException
    @ExceptionHandler(MyNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<MyErrorResponse> handleNotAcceptableException(MyNotAcceptableException ex) {
        return new ResponseEntity<>(ex.getResponse(), HttpStatus.NOT_ACCEPTABLE);
    }

    // Handle MyAlreadyExistException
    @ExceptionHandler(MyAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<MyErrorResponse> handleMyAlreadyExistException(MyAlreadyExistException ex) {
        return new ResponseEntity<>(ex.getResponse(), HttpStatus.CONFLICT);
    }

    // Handle IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Handle NullPointerException
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        return new ResponseEntity<>("A required value was null.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle generic Exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle MyNotFoundException
    @ExceptionHandler(MyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<MyErrorResponse> handleMyNotFoundException(MyNotFoundException ex) {
        return new ResponseEntity<>(ex.getResponse(), HttpStatus.NOT_FOUND);
    }

    // Handle ValidationException
        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<MyErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
                List<MyError> errors = ex.getBindingResult().getFieldErrors().stream()
                                .map(this::mapFieldError)
                                .collect(Collectors.toList());

                MyErrorResponse resposne = MyErrorResponse.builder()
                                .message("Validation failed")
                                .errors(errors)
                                .build();

                return new ResponseEntity<>(resposne, HttpStatus.BAD_REQUEST);
        }

        // Helper method to map field errors
        private MyError mapFieldError(FieldError fieldError) {
                return MyError.builder()
                                .key(fieldError.getField())
                                .message(fieldError.getDefaultMessage())
                                .build();
        }
}
