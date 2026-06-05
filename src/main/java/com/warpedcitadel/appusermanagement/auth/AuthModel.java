package com.warpedcitadel.appusermanagement.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthModel {

    private final String uuid;
    private final String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final String passwordHash;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final String role;


    public AuthModel(String uuid, String username, String passwordHash, String role) {
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
        return role;
    }

}
