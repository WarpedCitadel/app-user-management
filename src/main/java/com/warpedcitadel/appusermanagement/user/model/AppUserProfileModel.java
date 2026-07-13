package com.warpedcitadel.appusermanagement.user.model;

import java.util.List;

public class AppUserProfileModel {

    private long appUserId;
    private String uuid;
    private String profileImg;
    private String displayName;
    private String bio;
    private List<GameProfileModel> createdGames;


    public AppUserProfileModel() {

    }

    public AppUserProfileModel(String uuid, String displayName) {
        this.uuid = uuid;
        this.displayName = displayName;
    }

    public AppUserProfileModel(String uuid, String displayName, String bio, String profileImg, List<GameProfileModel> createdGames) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.bio = bio;
        this.profileImg = profileImg;
        this.createdGames = createdGames;
    }

    public AppUserProfileModel(long appUserId, String uuid, String displayName, String bio) {
        this.appUserId = appUserId;
        this.uuid = uuid;
        this.displayName = displayName;
        this.bio = bio;
    }

    public long getAppUserId() {
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

    public String getProfileImg() {
        return profileImg;
    }

    public List<GameProfileModel> getCreatedGames() {
        return createdGames;
    }
}
