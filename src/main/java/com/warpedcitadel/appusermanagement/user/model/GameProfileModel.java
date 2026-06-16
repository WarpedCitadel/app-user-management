package com.warpedcitadel.appusermanagement.user.model;

public class GameProfileModel {

    private String gameProfileUUID;
    private String title;
    private String coverImgUUID;
    private String description;
    private String genre;


    GameProfileModel () {

    }

    public GameProfileModel(String gameProfileUUID, String title, String coverImgUUID,
                            String description, String genre) {
        this.gameProfileUUID = gameProfileUUID;
        this.title = title;
        this.coverImgUUID = coverImgUUID;
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

    public String getCoverImgUUID() {
        return coverImgUUID;
    }

    public void setCoverImgUUID(String coverImgUUID) {
        this.coverImgUUID = coverImgUUID;
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
