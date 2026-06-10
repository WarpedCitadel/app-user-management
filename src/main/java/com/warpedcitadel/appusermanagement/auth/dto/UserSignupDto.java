package com.warpedcitadel.appusermanagement.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.warpedcitadel.appusermanagement.auth.validation.EmailFormat;
import com.warpedcitadel.appusermanagement.auth.validation.PasswordFormat;
import com.warpedcitadel.appusermanagement.auth.validation.UsernameFormat;

public record UserSignupDto(

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @UsernameFormat(message = "Invalid username")
        String username,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @PasswordFormat(message = "Invalid password")
        String password,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @EmailFormat(message = "Invalid email")
        String email
) {}
