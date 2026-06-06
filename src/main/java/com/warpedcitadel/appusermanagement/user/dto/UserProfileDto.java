package com.warpedcitadel.appusermanagement.user.dto;

import com.warpedcitadel.appusermanagement.user.model.GameProfileModel;
import jakarta.annotation.Nullable;

import java.util.List;

public record UserProfileDto(
        String userUUID,
        String profileImgUUID,
        String displayName,
        String biography,

        @Nullable
        List<GameProfileModel> createdGames
) {}
