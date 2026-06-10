package com.warpedcitadel.appusermanagement.management.dto;

import com.warpedcitadel.appusermanagement.management.model.UserDetailsModel;

public record GetAppUsersDto(
        SlicedResponse<UserDetailsModel> listUsers
) {}
