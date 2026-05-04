package com.warpedcitadel.appusermanagement.exceptionhandlers.exceptions;

import java.sql.SQLIntegrityConstraintViolationException;


// Todo | Exception goes through put may need to specify desired response
public class UserAlreadyExistsException extends SQLIntegrityConstraintViolationException {
    public UserAlreadyExistsException(String message, Throwable cause){
        super(message, cause);
    }
}
