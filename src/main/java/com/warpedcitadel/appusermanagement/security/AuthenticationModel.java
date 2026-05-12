package com.warpedcitadel.appusermanagement.security;

public class AuthenticationModel {

    private final String uuid;
    private final String username;
    private final String passwordHash;
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
