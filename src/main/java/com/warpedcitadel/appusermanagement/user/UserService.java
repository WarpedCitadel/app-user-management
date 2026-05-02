package com.warpedcitadel.appusermanagement.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public boolean loginUser(String username, String passwordHash, String email) {
        return repository.authenticateUser(username, passwordHash, email);
    }

    public long registerUser(UserModel user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String encodedPassword = encoder.encode(user.getPasswordHash());
        user.setPasswordHash(encodedPassword);
        return repository.registerUser(user);
    }
}
