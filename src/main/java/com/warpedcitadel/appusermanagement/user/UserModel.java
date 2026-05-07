package com.warpedcitadel.appusermanagement.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warpedcitadel.appusermanagement.user.validation.EmailFormat;
import com.warpedcitadel.appusermanagement.user.validation.PasswordFormat;
import com.warpedcitadel.appusermanagement.user.validation.UsernameFormat;

import java.util.Locale;

public class UserModel {

    @JsonIgnore
    private long appUserId;

    @UsernameFormat(message = "Invalid username")
    private String username;

    @PasswordFormat(message = "Invalid password")
    private String passwordHash;

    @EmailFormat(message = "Invalid email")
    private String email;


    public UserModel() {

    }

    public UserModel(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
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
        return email = email.toLowerCase(Locale.ROOT);
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    // I am not even using this lol
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
