package com.warpedcitadel.appusermanagement.management.dto;

public record SearchAttributesDto(
        String displayName,
        String role,
        Boolean isActive
) {}
