package com.warpedcitadel.appusermanagement.management.dto;

public record UserDetailsDto(
        String uuid,
        String profileImg,
        String username,
        String displayName,
        String email,
        String role,
        boolean isActive,
        String createdDtm
) {}