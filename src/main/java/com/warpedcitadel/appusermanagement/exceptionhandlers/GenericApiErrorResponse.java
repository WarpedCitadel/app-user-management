package com.warpedcitadel.appusermanagement.exceptionhandlers;

import java.time.Instant;
import java.util.Map;

// Todo | Make this generic for all user input errors / exceptions
public record GenericApiErrorResponse<timestamp>(
        String title,
        int status,
        Map<String, String> errors,
        String instance,
        Instant timestamp
) {}
