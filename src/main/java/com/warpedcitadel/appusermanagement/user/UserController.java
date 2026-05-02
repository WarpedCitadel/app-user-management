package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.payload.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestController
@RequestMapping(path = "/app_user")
public class UserController {

    @Autowired
    private UserService userService;

    // TODO add response body
    @PostMapping("/login")
    private String getAppUser(@RequestBody UserModel user) {
        boolean success = userService.loginUser(user.getUsername(),
                user.getPasswordHash(), user.getEmail());

        return success ? "Account Created!" : "Invalid Inputs!";
    }


    @PostMapping("/register")
    private ResponseEntity<ApiResponse<UserModel>> createAppUser(@Valid @RequestBody UserModel user, WebRequest request){
        userService.registerUser(user);
        ApiResponse<UserModel> response = new ApiResponse<>("User Created", HttpStatus.CREATED.value(),
                user, request.getDescription(false).replace("uri=", ""), Instant.now());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
