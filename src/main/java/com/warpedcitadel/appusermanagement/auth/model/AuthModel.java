package com.warpedcitadel.appusermanagement.auth.model;

public class AuthModel {

    private final String uuid;
    private final String username;
    private final String passwordHash;
    private final String role;
    private final boolean isActive;


    public AuthModel(String uuid, String username, String passwordHash, String role, boolean isActive) {
        this.uuid = uuid;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.isActive = isActive;
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

    public boolean isActive() {
        return isActive;
    }
}
