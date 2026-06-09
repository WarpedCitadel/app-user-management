package com.warpedcitadel.appusermanagement.payload;

import java.time.Instant;

public record ApiResponse<Var>(
        String title,
        int status,
        Var data,
        String instance,
        Instant timestamp
) {}
