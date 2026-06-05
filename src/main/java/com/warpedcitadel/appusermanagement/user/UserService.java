package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.user.model.AppUserProfileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public int createUserProfile(AppUserProfileModel updateProfileDetails){
        int userId = userRepository.getUserIdByUuid(updateProfileDetails.getUuid());
        AppUserProfileModel appUserProfileModel = new AppUserProfileModel(userId, updateProfileDetails.getUuid(),
                updateProfileDetails.getDisplayName(), updateProfileDetails.getBio());

        return userRepository.createAppUserProfile(appUserProfileModel);
    }


    public boolean updateUserProfile(AppUserProfileModel updateProfileDetails, String username) {
        int userId = userRepository.getUserIdByUsername(username);
        int userProfileId = userRepository.getUserIdByUuid(updateProfileDetails.getUuid());

        if (userProfileId == userId) {
            AppUserProfileModel appUserProfileModel = new AppUserProfileModel(userProfileId, updateProfileDetails.getUuid(),
                    updateProfileDetails.getDisplayName(), updateProfileDetails.getBio());
            userRepository.updateAppUserProfile(appUserProfileModel);
            return true;
        }
        return false;
    }
}
