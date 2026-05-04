package com.warpedcitadel.appusermanagement.exceptionhandlers;

import java.time.Instant;

// Todo | Make this generic for database based errors / exceptions
public record UniqueConstraintApiError(
        String title,
        int status,
        String error, // need to map out the errors
        String instance,
        Instant timestamp
) {}
