package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.user.validation.ValidEmailFormat;

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


    public boolean dtoValidation(){

        ValidEmailFormat emailValidator = new ValidEmailFormat();

        if (isNull()) {
            System.out.println("Null Value detected!");
            return true;
        } else {
            if (isBlank()){
                System.out.println("Empty value detected!");
                return true;
            } else {
                if (!emailValidator.isValid(getEmail())){
                    System.out.println("Not a valid Email");
                    return true;
                } else {
                    System.out.println("Email is Valid");
                    return false;
                }
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
