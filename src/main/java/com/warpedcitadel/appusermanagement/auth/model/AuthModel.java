package com.warpedcitadel.appusermanagement.auth.model;

public class AuthModel {

    private final String uuid;
    private final String username;
    private final String passwordHash;
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
