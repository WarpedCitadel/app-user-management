package com.warpedcitadel.appusermanagement.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/app_user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    private String getAppUser(@RequestBody UserModel user) {
        boolean success = userService.loginUser(user.getUsername(),
                user.getPasswordHash(), user.getEmail());

        return success ? "Account Created!" : "Invalid Inputs!";
    }

    @PostMapping("/register")
    private String createAppUser(@RequestBody UserModel user) {
        long appUserId = userService.registerUser(user);
        return appUserId > 0 ? "User logged in with AppUserId of " +
                appUserId : "Login Failed! Invalid Credentials";
    }
}
