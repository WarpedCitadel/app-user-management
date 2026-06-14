package com.warpedcitadel.appusermanagement.auth.dto;

import jakarta.annotation.Nullable;

public record UserVerificationDto(
        String email,

        @Nullable
        String sessionToken
) {}
