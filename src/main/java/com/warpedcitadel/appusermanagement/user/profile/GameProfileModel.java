package com.warpedcitadel.appusermanagement.user.profile;

public record GameProfileModel(
        String fileUUID,
        String title,
        String coverImgUUID,
        String description,
        String genre
)
{}
