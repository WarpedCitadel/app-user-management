package com.warpedcitadel.appusermanagement.payload;

import java.time.Instant;

public record ApiResponse<Var>(
        String title,
        int status,
        Var user,
        String instance,
        Instant timestamp
) {}
