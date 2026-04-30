package com.warpedcitadel.appusermanagement.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public boolean loginUser(String username, String passwordHash, String email) {
        return repository.authenticateUser(username, passwordHash, email);
    }

    public long registerUser(UserModel user) {
        return repository.registerUser(user);
    }
}
