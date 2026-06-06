package com.warpedcitadel.appusermanagement.payload;

import java.time.Instant;
import java.util.Map;


public record GenericApiErrorResponse<timestamp>(
        String title,
        int status,
        Map<String, String> errors,
        String instance,
        Instant timestamp
) {}
