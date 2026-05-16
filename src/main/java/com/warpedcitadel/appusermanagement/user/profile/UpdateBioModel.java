package com.warpedcitadel.appusermanagement.user.profile;

public class UpdateBioModel {

    private int appUserId;
    private String uuid;
    private String bio;


    public UpdateBioModel() {

    }

    public UpdateBioModel(int appUserId, String bio){
        this.appUserId = appUserId;
        this.bio = bio;
    }

    public UpdateBioModel(String uuid, String bio) {
        this.uuid = uuid;
        this.bio = bio;
    }


    public int getAppUserId() {
        return appUserId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getBio() {
        return bio;
    }
}
