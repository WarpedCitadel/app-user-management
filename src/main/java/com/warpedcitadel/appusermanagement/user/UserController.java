package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.payload.LoginApiResponse;
import com.warpedcitadel.appusermanagement.payload.RegisterApiResponse;
import com.warpedcitadel.appusermanagement.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // TODO add response body
    @PostMapping("/login")
    private ResponseEntity<LoginApiResponse<UserModel>> loginUser(@RequestBody UserModel user, WebRequest request){
        String token = jwtUtil.generateToken(user.getUsername());
        if (userService.loginUser(user)) {
            LoginApiResponse<UserModel> response = new LoginApiResponse<>("Logged in", HttpStatus.OK.value(),
                    user, token, request.getDescription(false).replace("uri=", ""), Instant.now());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/register")
    private ResponseEntity<RegisterApiResponse<UserModel>> createAppUser(@Valid @RequestBody UserModel user, WebRequest request){
        userService.registerUser(user);
        RegisterApiResponse<UserModel> response = new RegisterApiResponse<>("User Created", HttpStatus.CREATED.value(),
                user, request.getDescription(false).replace("uri=", ""), Instant.now());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
