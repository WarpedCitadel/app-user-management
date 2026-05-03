package com.warpedcitadel.appusermanagement.exceptionhandlers.exceptions;

import java.sql.SQLIntegrityConstraintViolationException;

public class UserAlreadyExistsException extends SQLIntegrityConstraintViolationException {
    public UserAlreadyExistsException(String message, Throwable cause){
        super(message, cause);
    }
}
