package com.warpedcitadel.appusermanagement.user.profile;

public record GameProfileModel(
        String uuid,
        String title,
        String image,
        String description,
        String genre
)
{}
