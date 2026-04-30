package com.warpedcitadel.appusermanagement.user;


public class UserModel {


    private long appUserId;
    private String username;
    private String passwordHash;
    private String email;


    public UserModel() {

    }


    public UserModel(String username, String passwordHash, String email) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }


    public long getAppUserId() {
        return appUserId;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }


    public void setAppUserId(long appUserId) {
        this.appUserId = appUserId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                " appUserId=" + appUserId +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
