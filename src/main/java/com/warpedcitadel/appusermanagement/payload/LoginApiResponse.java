package com.warpedcitadel.appusermanagement.payload;

import java.time.Instant;

public record LoginApiResponse<UserModel>(
        String title,
        int status,
        UserModel user,
        String token,
        String instance,
        Instant timestamp
) {}
