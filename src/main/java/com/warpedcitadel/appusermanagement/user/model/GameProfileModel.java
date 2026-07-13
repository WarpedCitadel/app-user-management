package com.warpedcitadel.appusermanagement.user.model;

public class GameProfileModel {

    private String gameProfileUUID;
    private String title;
    private String profileImage;
    private String description;
    private String genre;


    GameProfileModel () {

    }

    public GameProfileModel(String gameProfileUUID, String title, String profileImage,
                            String description, String genre) {
        this.gameProfileUUID = gameProfileUUID;
        this.title = title;
        this.profileImage = profileImage;
        this.description = description;
        this.genre = genre;
    }

    public String getGameProfileUUID() {
        return gameProfileUUID;
    }

    public void setGameProfileUUID(String gameProfileUUID) {
        this.gameProfileUUID = gameProfileUUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
