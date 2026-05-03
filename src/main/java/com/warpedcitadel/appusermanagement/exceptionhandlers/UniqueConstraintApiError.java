package com.warpedcitadel.appusermanagement.exceptionhandlers;

import java.time.Instant;

public record UniqueConstraintApiError(
        String title,
        int status,
        String error, // need to map out the errors
        String instance,
        Instant timestamp
) {}
