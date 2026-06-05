package com.warpedcitadel.appusermanagement.user.model;

public record GameProfileModel(
        String fileUUID,
        String title,
        String coverImgUUID,
        String description,
        String genre
)
{}
