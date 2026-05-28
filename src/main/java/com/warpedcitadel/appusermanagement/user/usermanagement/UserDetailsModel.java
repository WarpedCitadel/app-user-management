package com.warpedcitadel.appusermanagement.user.usermanagement;

public class UserDetailsModel {

    String uuid;
    String profileIMG;
    String displayName;
    String email;
    String role;
    boolean isActive;
    String createdDtm;


    public UserDetailsModel() {

    }

    public UserDetailsModel(String uuid, String profileIMG, String displayName, String email, String role, boolean isActive, String createdDtm) {
        this.uuid = uuid;
        this.profileIMG = profileIMG;
        this.displayName = displayName;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
        this.createdDtm = createdDtm;
    }


    public String getUuid() {
        return uuid;
    }

    public String getProfileIMG() {
        return profileIMG;
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
