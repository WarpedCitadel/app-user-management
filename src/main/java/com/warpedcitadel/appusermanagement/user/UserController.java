package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.payload.ApiResponse;
import com.warpedcitadel.appusermanagement.security.AuthenticationModel;
import com.warpedcitadel.appusermanagement.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    private ResponseEntity<ApiResponse<AuthenticationModel>> loginUser(@RequestBody UserModel user, WebRequest request){
        AuthenticationModel dbUser = userService.loginUser(user);
            ApiResponse<AuthenticationModel> response = new ApiResponse<>("Logged in", HttpStatus.OK.value(),
                    dbUser, request.getDescription(false).replace("uri=", ""), Instant.now());
            String jwtToken = jwtUtil.generateToken(user.getUsername());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }


    @PostMapping("/register")
    private ResponseEntity<ApiResponse<UserModel>> createAppUser(@Valid @RequestBody UserModel user, WebRequest request){
        userService.registerUser(user);
        ApiResponse<UserModel> response = new ApiResponse<>("User Created", HttpStatus.CREATED.value(),
                user, request.getDescription(false).replace("uri=", ""), Instant.now());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
