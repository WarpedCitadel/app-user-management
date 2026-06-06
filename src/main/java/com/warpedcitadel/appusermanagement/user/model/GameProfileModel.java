package com.warpedcitadel.appusermanagement.user.model;

public class GameProfileModel {

    private String fileUUID;
    private String title;
    private String coverImgUUID;
    private String description;
    private String genre;


    GameProfileModel () {

    }

    public GameProfileModel(String fileUUID, String title, String coverImgUUID,
                            String description, String genre) {
        this.fileUUID = fileUUID;
        this.title = title;
        this.coverImgUUID = coverImgUUID;
        this.description = description;
        this.genre = genre;
    }

    public String getFileUUID() {
        return fileUUID;
    }

    public void setFileUUID(String fileUUID) {
        this.fileUUID = fileUUID;
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
