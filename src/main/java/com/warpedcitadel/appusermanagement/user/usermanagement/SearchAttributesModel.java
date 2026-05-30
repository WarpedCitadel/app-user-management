package com.warpedcitadel.appusermanagement.user.usermanagement;

public class SearchAttributesModel {

    private String displayName;
    private String role;
    private Boolean isActive;


    public SearchAttributesModel(String displayName, String role, Boolean isActive) {
        this.displayName = displayName;
        this.role = role;
        this.isActive = isActive;
    }


    public String getDisplayName() {
        return displayName;
    }

    public String getRole() {
        return role;
    }

    public Boolean getIsActive() {
        return isActive;
    }
}
