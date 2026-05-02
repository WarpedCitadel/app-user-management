package com.warpedcitadel.appusermanagement.exceptionhandlers;

import java.time.Instant;
import java.util.Map;

public record ApiErrorResponse<timestamp>(
        String title,
        int status,
        Map<String, String> errors,
        String instance,
        Instant timestamp
) {}
