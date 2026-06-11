package com.warpedcitadel.appusermanagement.auth;

import com.warpedcitadel.appusermanagement.auth.dto.UserReferenceDto;
import com.warpedcitadel.appusermanagement.auth.dto.UserLoginDto;
import com.warpedcitadel.appusermanagement.auth.dto.UserSignupDto;
import com.warpedcitadel.appusermanagement.payload.ApiResponse;
import com.warpedcitadel.appusermanagement.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    private ResponseEntity<ApiResponse<UserReferenceDto>> loginAppUser(@RequestBody UserLoginDto user,
                                                                       WebRequest request) {
        UserReferenceDto userDto = authService.loginUser(user);

        ApiResponse<UserReferenceDto> response = new ApiResponse<>("Logged in",
                HttpStatus.OK.value(),
                userDto,
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));

            String jwtToken = jwtUtil.generateToken(user.username());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);

            return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }


    @PostMapping("/signup")
    private ResponseEntity<ApiResponse<String>> createAppUser(@Valid @RequestBody UserSignupDto user,
                                                              WebRequest request) {
        authService.createAppUser(user);

        ApiResponse<String> response = new ApiResponse<>("Signup",
                HttpStatus.CREATED.value(),
                "Account successfully created",
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/activate")
    private ResponseEntity<ApiResponse<String>> activateAccount(@RequestParam("token") String token, WebRequest request) {

        ApiResponse<String> response = new ApiResponse<>("Signup",
                HttpStatus.OK.value(),
                "Account activated",
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
