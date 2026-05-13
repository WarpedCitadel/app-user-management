package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.security.AuthenticationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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


    // Todo | procure a JWT to the logged user
    public AuthenticationModel loginUser(UserModel user) {
        AuthenticationModel dbUser = repository.authenticateUser(user.getUsername());
        String storedHash = dbUser.getPasswordHash();
        if (BCrypt.checkpw(user.getPasswordHash(), storedHash)) {
            return dbUser;
        }
        throw new RuntimeException("User password does not match!");
    }

    public int registerUser(UserModel user) {
        String encodedPassword = passwordEncoder().encode(user.getPasswordHash());
        user.setPasswordHash(encodedPassword);
        return repository.registerUser(user);
    }
}
