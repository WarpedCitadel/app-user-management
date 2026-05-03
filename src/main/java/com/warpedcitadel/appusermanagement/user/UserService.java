package com.warpedcitadel.appusermanagement.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public boolean loginUser(String username, String passwordHash) {
        return repository.authenticateUser(username, passwordHash);
    }

    public long registerUser(UserModel user) {
        String encodedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(encodedPassword);
        return repository.registerUser(user);
    }
}
