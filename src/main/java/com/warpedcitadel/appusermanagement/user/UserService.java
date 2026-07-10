package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.user.dto.UserProfileDto;
import com.warpedcitadel.appusermanagement.user.model.AppUserProfileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public UserProfileDto getUserProfile(String uuid) {
        AppUserProfileModel userProfileModel = userRepository.getAppUserProfile(uuid);

        UserProfileDto userProfile = new UserProfileDto(
                userProfileModel.getUuid(),
                userProfileModel.getProfileIMG(),
                userProfileModel.getDisplayName(),
                userProfileModel.getBio(),
                userProfileModel.getCreatedGames()
        );

        return userProfile;
    }

    public void createUserProfile(UserProfileDto createProfile){

        long userProfileId = userRepository.getUserIdByUuid(createProfile.userUUID());

        AppUserProfileModel appUserProfileModel =
                new AppUserProfileModel(userProfileId, createProfile.userUUID(),
                createProfile.displayName(), createProfile.biography());

        userRepository.createAppUserProfile(appUserProfileModel);
    }


    public void updateUserProfile(UserProfileDto updateProfile) {

        long userProfileId = userRepository.getUserIdByUuid(updateProfile.userUUID());

        AppUserProfileModel appUserProfileModel =
                new AppUserProfileModel(userProfileId, updateProfile.userUUID(),
                        updateProfile.displayName(), updateProfile.biography());

        userRepository.updateAppUserProfile(appUserProfileModel);
    }
}
