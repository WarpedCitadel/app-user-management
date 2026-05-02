package com.warpedcitadel.appusermanagement.payload;

import java.time.Instant;

public record ApiResponse<UserModel>(
        String title,
        int status,
        UserModel user,
        String instance,
        Instant timestamp
) {}
