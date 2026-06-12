package com.warpedcitadel.appusermanagement.auth.dto;

public record VerificationTokenDto(
        String token,
        String passcode
) {}
