package com.warpedcitadel.appusermanagement.audit.dto;

import java.util.List;

public record UserSessionsDto(
        List<String> sessions
) {}
