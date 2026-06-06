package com.warpedcitadel.appusermanagement.management.dto;

import com.warpedcitadel.appusermanagement.management.model.UserDetailsModel;
import org.springframework.data.domain.Slice;

public record GetAppUsersDto(
        Slice<UserDetailsModel> listUsers
) {}
