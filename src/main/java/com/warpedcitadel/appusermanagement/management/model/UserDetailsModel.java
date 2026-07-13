package com.warpedcitadel.appusermanagement.management.model;

public class UserDetailsModel {

    String userUUID;
    String profileIMG;
    String username;
    String displayName;
    String email;
    String role;
    boolean isActive;
    String createdDtm;


    public UserDetailsModel() {

    }

    public UserDetailsModel(String userUUID, String profileIMG,
                            String displayName, String username, String email,
                            String role, boolean isActive, String createdDtm) {

        this.userUUID = userUUID;
        this.profileIMG = profileIMG;
        this.displayName = displayName;
        this.username = username;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
        this.createdDtm = createdDtm;
    }


    public String getUserUUID() {
        return userUUID;
    }

    public String getProfileIMG() {
        return profileIMG;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getCreatedDtm() {
        return createdDtm;
    }
}
