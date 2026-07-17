package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.user.dto.UserProfileDto;
import com.warpedcitadel.appusermanagement.user.model.AppUserProfileModel;
import com.warpedcitadel.appusermanagement.util.CloudFrontService;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CloudFrontService cloudFrontService;

    public UserService(UserRepository userRepository, CloudFrontService cloudFrontService) {
        this.userRepository = userRepository;
        this.cloudFrontService = cloudFrontService;
    }


    public UserProfileDto getUserProfile(String uuid) {
        AppUserProfileModel userProfileModel = userRepository.getAppUserProfile(uuid);

        String profilePrefix = "images/users/" + userProfileModel.getFileUUID() + "/image/" + userProfileModel.getFileName();
        String profileImgUrl = cloudFrontService.generateSignedUrl(profilePrefix);

        for (int i = 0; userProfileModel.getCreatedGames().size() > i; i++) {

            String gameProfileUUID = userProfileModel.getCreatedGames().get(i).getGameProfileUUID();
            String fileName = userProfileModel.getCreatedGames().get(i).getProfileImage();

            String gamePrefix = "images/games/" + gameProfileUUID + "/gameImages/" + fileName;
            String gameImageUrl = cloudFrontService.generateSignedUrl(gamePrefix);
            userProfileModel.getCreatedGames().get(i).setProfileImage(gameImageUrl);
        }

        UserProfileDto userProfile = new UserProfileDto(
                userProfileModel.getUuid(),
                profileImgUrl,
                userProfileModel.getDisplayName(),
                userProfileModel.getUsername(),
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
