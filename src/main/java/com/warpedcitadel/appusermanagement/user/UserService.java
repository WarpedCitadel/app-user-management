package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.security.AuthenticationModel;
import com.warpedcitadel.appusermanagement.user.profile.AppUserProfileModel;
import com.warpedcitadel.appusermanagement.user.usermanagement.SearchAttributesModel;
import com.warpedcitadel.appusermanagement.user.usermanagement.UserDetailsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Bean
    private PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder(10);
    }


    public AuthenticationModel loginUser(UserModel user) {
        AuthenticationModel dbUser = repository.authenticateUser(user.getUsername());
        String storedHash = dbUser.getPasswordHash();
        if (user.getUsername().equals(dbUser.getUsername())){
            if (BCrypt.checkpw(user.getPasswordHash(), storedHash)) {

                repository.updateLastActiveDtm(dbUser.getUuid());
                return dbUser;
            }
        }
        throw new UsernameNotFoundException("Invalid user name or password");
    }

    public int registerUser(UserModel user) {
        String encodedPassword = passwordEncoder().encode(user.getPasswordHash());
        user.setPasswordHash(encodedPassword);

        return repository.registerUser(user);
    }


    public int createUserProfile(AppUserProfileModel updateProfileDetails){
        int userId = repository.getUserIdByUuid(updateProfileDetails.getUuid());
        AppUserProfileModel appUserProfileModel = new AppUserProfileModel(userId, updateProfileDetails.getUuid(),
                updateProfileDetails.getDisplayName(), updateProfileDetails.getBio());

        return repository.createAppUserProfile(appUserProfileModel);
    }


    public boolean updateUserProfile(AppUserProfileModel updateProfileDetails, String username) {
        int userId = repository.getUserIdByUsername(username);
        int userProfileId = repository.getUserIdByUuid(updateProfileDetails.getUuid());

        if (userProfileId == userId) {
            AppUserProfileModel appUserProfileModel = new AppUserProfileModel(userProfileId, updateProfileDetails.getUuid(),
                    updateProfileDetails.getDisplayName(), updateProfileDetails.getBio());
            repository.updateAppUserProfile(appUserProfileModel);
            return true;
        }
        return false;
    }


    protected Slice<UserDetailsModel> getAppUsers(Pageable pageable, SearchAttributesModel attributes) {
        return repository.getAppUsers(pageable, attributes);
    }
}
