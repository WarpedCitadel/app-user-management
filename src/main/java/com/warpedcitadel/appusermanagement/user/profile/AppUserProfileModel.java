package com.warpedcitadel.appusermanagement.user.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AppUserProfileModel {

    @JsonIgnore
    private int appUserId;

    private String uuid;
    private String displayName;
    private String bio;


    public AppUserProfileModel() {

    }

    public AppUserProfileModel(String uuid, String displayName, String bio) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.bio = bio;
    }

    public AppUserProfileModel(int appUserId, String uuid, String displayName, String bio) {
        this.appUserId = appUserId;
        this.uuid = uuid;
        this.displayName = displayName;
        this.bio = bio;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }
}
