package com.warpedcitadel.appusermanagement.auth;

import com.warpedcitadel.appusermanagement.payload.ApiResponse;
import com.warpedcitadel.appusermanagement.security.JwtUtil;
import com.warpedcitadel.appusermanagement.user.model.UserModel;
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

import java.time.Clock;
import java.time.Instant;

@RestController
@RequestMapping(path = "/auth", version = "1.0")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    private ResponseEntity<ApiResponse<AuthModel>> loginUser(@RequestBody UserModel user, WebRequest request) {
        AuthModel dbUser = authService.loginUser(user);
            ApiResponse<AuthModel> response = new ApiResponse<>("Logged in", HttpStatus.OK.value(),
                    dbUser, request.getDescription(false).replace("uri=", ""), Instant.now(Clock.systemUTC()));
            String jwtToken = jwtUtil.generateToken(user.getUsername());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }


    @PostMapping("/signin")
    private ResponseEntity<ApiResponse<UserModel>> createAppUser(@Valid @RequestBody UserModel user, WebRequest request) {
        authService.registerUser(user);
        ApiResponse<UserModel> response = new ApiResponse<>("User Created", HttpStatus.CREATED.value(),
                user, request.getDescription(false).replace("uri=", ""), Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
