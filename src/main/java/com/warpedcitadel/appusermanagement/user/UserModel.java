package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.user.validation.ValidEmailFormat;
import com.warpedcitadel.appusermanagement.user.validation.ValidPasswordFormat;
import com.warpedcitadel.appusermanagement.user.validation.ValidUsernameFormat;

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
        return username.strip();
    }

    public String getPasswordHash() {
        return passwordHash.strip();
    }

    public String getEmail() {
        return email.replaceAll("\\s", "");
    }

// TODO: add proper http client responses
    public boolean dtoValidation(){

        ValidEmailFormat emailValidator = new ValidEmailFormat();
        ValidPasswordFormat passwordValidator = new ValidPasswordFormat();
        ValidUsernameFormat usernameValidator = new ValidUsernameFormat();

        if (isNull()) {
            System.out.println("Null Value detected!");
            return true;
        } else {
            if (isBlank()){
                System.out.println("Empty value detected!");
                return true;
            } else {
                if (!usernameValidator.isValid(getUsername())) {
                    System.out.println("Not a valid username");
                    return true;
                }
                if (!passwordValidator.isValid(getPasswordHash())) {
                    System.out.println("Not a valid password");
                    return true;
                }
                if (!emailValidator.isValid(getEmail())) {
                    System.out.println("Not a valid email");
                    return true;
                }
                return false;
            }
        }
    }


    private boolean isBlank() {
        return getUsername().isBlank() || getPasswordHash().isBlank() || getEmail().isBlank();
    }

    private boolean isNull(){
        return getUsername().isEmpty() || getPasswordHash().isEmpty() || getEmail().isEmpty();
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
