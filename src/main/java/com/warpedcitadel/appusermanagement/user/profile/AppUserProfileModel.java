package com.warpedcitadel.appusermanagement.user.profile;

public class AppUserProfileModel {

    private String uuid;
    private String username;
    private String bio;

    public AppUserProfileModel(String uuid, String username, String bio) {
        this.uuid = uuid;
        this.username = username;
        this.bio = bio;
    }


    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }
}
