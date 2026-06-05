package com.warpedcitadel.appusermanagement.management.model;

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

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
