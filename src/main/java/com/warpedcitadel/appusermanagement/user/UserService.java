package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.security.AuthenticationModel;
import com.warpedcitadel.appusermanagement.user.profile.UpdateBioModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }


    public AuthenticationModel loginUser(UserModel user) {
        AuthenticationModel dbUser = repository.authenticateUser(user.getUsername());
        String storedHash = dbUser.getPasswordHash();
        if (user.getUsername().equals(dbUser.getUsername())){
            if (BCrypt.checkpw(user.getPasswordHash(), storedHash)) {
                return dbUser;
            }
        }
        throw new BadCredentialsException("Invalid Username or password!");
    }

    public int registerUser(UserModel user) {
        String encodedPassword = passwordEncoder().encode(user.getPasswordHash());
        user.setPasswordHash(encodedPassword);
        return repository.registerUser(user);
    }


    public int updateUserBio(UpdateBioModel updateBioDetails){
        int userId = repository.getUserId(updateBioDetails.getUuid());
        UpdateBioModel updateBio = new UpdateBioModel(userId, updateBioDetails.getBio());
        return repository.updateDbBio(updateBio);
    }
}
