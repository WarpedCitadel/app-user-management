package com.warpedcitadel.appusermanagement.user.model;

import java.util.List;

public class AppUserProfileModel {

    private long appUserId;
    private String uuid;
    private String fileUUID;
    private String fileName;
    private String displayName;
    private String username;
    private String bio;
    private List<GameProfileModel> createdGames;


    public AppUserProfileModel() {

    }

    public AppUserProfileModel(String uuid, String displayName) {
        this.uuid = uuid;
        this.displayName = displayName;
    }

    public AppUserProfileModel(String uuid, String displayName, String username, String bio, String fileUUID, String fileName, List<GameProfileModel> createdGames) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.username = username;
        this.bio = bio;
        this.fileUUID = fileUUID;
        this.fileName = fileName;
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

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }

    public String getFileUUID() {
        return fileUUID;
    }

    public String getFileName() {
        return fileName;
    }

    public List<GameProfileModel> getCreatedGames() {
        return createdGames;
    }
}
