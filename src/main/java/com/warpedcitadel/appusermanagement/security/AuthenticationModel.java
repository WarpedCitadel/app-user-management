package com.warpedcitadel.appusermanagement.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationModel {

    private final String uuid;
    private final String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final String passwordHash;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String role;


    public AuthenticationModel(String uuid, String username, String passwordHash, String role) {
        this.uuid = uuid;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }


    public String getUuid(){
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole(){
        return role = role;
    }

}
