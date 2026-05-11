package com.warpedcitadel.appusermanagement.payload;

import java.time.Instant;

public record RegisterApiResponse<UserModel>(
        String title,
        int status,
        UserModel user,
        String instance,
        Instant timestamp
) {}
