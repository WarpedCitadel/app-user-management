package com.warpedcitadel.appusermanagement.user.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppUserProfileModel {

    @JsonIgnore
    private int appUserId;
    private String uuid;
    private String displayName;
    private String bio;
    private List<GameProfileModel> game;


    public AppUserProfileModel() {

    }

    public AppUserProfileModel(String uuid, String displayName) {
        this.uuid = uuid;
        this.displayName = displayName;
    }

    public AppUserProfileModel(String uuid, String displayName, String bio, List<GameProfileModel> game) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.bio = bio;
        this.game = game;
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

    public List<GameProfileModel> getGame() {
        return game;
    }
}
