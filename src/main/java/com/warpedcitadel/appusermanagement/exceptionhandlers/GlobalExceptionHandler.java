package com.warpedcitadel.appusermanagement.exceptionhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles invalid fields
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GenericApiErrorResponse handleValidationExceptions(MethodArgumentNotValidException methodArgumentNotValidException, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return new GenericApiErrorResponse<>(
                "Invalid fields",
                HttpStatus.BAD_REQUEST.value(),
                errors,
                request.getDescription(false).replace("uri=", ""),
                Instant.now()
        );
    }


    // Handles bad credentials
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public GenericApiErrorResponse handleBadCredentialsExceptions(BadCredentialsException badCredentialsException, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", badCredentialsException.getMessage());
        return new GenericApiErrorResponse<>(
                "Invalid fields",
                HttpStatus.UNAUTHORIZED.value(),
                errors,
                request.getDescription(false).replace("uri=", ""),
                Instant.now()
        );
    }


    // handles unique constraints exceptions from the database
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SQLException.class)
    public GenericApiErrorResponse handelUniqueConstraint(SQLException userAlreadyExistsException, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", userAlreadyExistsException.toString());
        return new GenericApiErrorResponse(
          "Can not replace existing value!",
          HttpStatus.CONFLICT.value(),
          errors,
          request.getDescription(false).replace("uri=", ""),
          Instant.now()
        );
    }
}
