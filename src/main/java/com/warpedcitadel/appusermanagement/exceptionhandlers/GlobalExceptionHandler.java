package com.warpedcitadel.appusermanagement.exceptionhandlers;

import com.warpedcitadel.appusermanagement.exceptionhandlers.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

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

    // Todo | Does not invoke the status 409 error and instead gets a 403.. may need to review security config
    // handles unique constraints exceptions from the database
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public UniqueConstraintApiError handelUniqueConstraint(UserAlreadyExistsException userAlreadyExistsException, WebRequest request) {
        return new UniqueConstraintApiError(
          "User already exists!",
          HttpStatus.CONFLICT.value(),
          userAlreadyExistsException.toString(),
          request.getDescription(false).replace("uri=", ""),
          Instant.now()
        );
    }
}
