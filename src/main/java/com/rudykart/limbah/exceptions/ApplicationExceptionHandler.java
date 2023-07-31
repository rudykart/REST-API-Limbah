package com.rudykart.limbah.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
// import org.springframework.web.servlet.NoHandlerFoundException;

import com.rudykart.limbah.dto.ErrorResponse;
import com.rudykart.limbah.dto.NotValidResponse;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String message = error.getDefaultMessage();
            List<String> fieldErrors = errorsMap.get(fieldName);

            if (fieldErrors == null) {
                fieldErrors = new ArrayList<>();
                errorsMap.put(fieldName, fieldErrors);
            }

            fieldErrors.add(message);
        });

        NotValidResponse notValid = new NotValidResponse("Validation errors", HttpStatus.BAD_REQUEST.value(),
                errorsMap);
        return new ResponseEntity<>(notValid, HttpStatus.BAD_REQUEST);
    }

    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<Object>
    // handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    // NotValidResponse notValid = new NotValidResponse("Validation errors",
    // HttpStatus.BAD_REQUEST.value(),
    // new HashMap<>());

    // ex.getBindingResult().getFieldErrors().forEach((error) -> {
    // String fieldName = error.getField();
    // String message = error.getDefaultMessage();
    // List<String> fieldErrors = notValid.getErrors().get(fieldName);

    // if (fieldErrors == null) {
    // fieldErrors = new ArrayList<>();
    // notValid.getErrors().put(fieldName, fieldErrors);
    // }

    // fieldErrors.add(message);
    // });
    // return new ResponseEntity<>(notValid, HttpStatus.BAD_REQUEST);
    // }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationFailException(AuthenticationFailedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CredentialNotMatchException.class)
    public ResponseEntity<ErrorResponse> handleCredentialNotMatchException(CredentialNotMatchException ex) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CashNotEnoughtException.class)
    public ResponseEntity<ErrorResponse> handleCashNotEnoughtException(CashNotEnoughtException ex) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
