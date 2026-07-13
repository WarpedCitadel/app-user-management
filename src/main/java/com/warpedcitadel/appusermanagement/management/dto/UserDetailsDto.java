package com.warpedcitadel.appusermanagement.management.dto;

public record UserDetailsDto(
        String userUUID,
        String profileImage,
        String username,
        String displayName,
        String email,
        String role,
        boolean isActive,
        String createdDtm
) {}