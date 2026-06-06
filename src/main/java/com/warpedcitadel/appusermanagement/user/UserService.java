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

    public void createUserProfile(UserProfileDto createProfile, String username){

        long userId = userRepository.getUserIdByUsername(username);
        long userProfileId = userRepository.getUserIdByUuid(createProfile.userUUID());

        System.out.println(userId + " == " + userProfileId);

        if (userProfileId != userId) {
            throw new RuntimeException("Action not allowed");
        }
        AppUserProfileModel appUserProfileModel =
                new AppUserProfileModel(userId, createProfile.userUUID(),
                createProfile.displayName(), createProfile.biography());

        userRepository.createAppUserProfile(appUserProfileModel);
    }


    public void updateUserProfile(UserProfileDto updateProfile, String username) {

        long userId = userRepository.getUserIdByUsername(username);
        long userProfileId = userRepository.getUserIdByUuid(updateProfile.userUUID());

        System.out.println(userId + " == " + userProfileId);

        if (userProfileId != userId) {
            throw new RuntimeException("Action not allowed");
        }
        AppUserProfileModel appUserProfileModel =
                new AppUserProfileModel(userProfileId, updateProfile.userUUID(),
                        updateProfile.displayName(), updateProfile.biography());

        userRepository.updateAppUserProfile(appUserProfileModel);
    }
}
