package com.warpedcitadel.appusermanagement.user;


import com.warpedcitadel.appusermanagement.payload.ApiResponse;
import com.warpedcitadel.appusermanagement.security.JwtUtil;
import com.warpedcitadel.appusermanagement.user.dto.UserProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.Instant;

@RestController
@RequestMapping(path = "/user", version = "1.0")
public class UserController {

    private static final String BEARER_ = "Bearer ";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    @PreAuthorize("hasAnyRole('admin', 'mod', 'user')")
    @PostMapping("/profile/createprofile")
    private ResponseEntity<ApiResponse> createUserProfile(@RequestBody UserProfileDto createProfile, WebRequest request) {

        String jwtToken = request.getHeader("Authorization");

        if (jwtToken == null || !jwtToken.startsWith(BEARER_)) {
            throw new RuntimeException("Action not allowed");
        }
        String cleanToken = jwtToken.substring(BEARER_.length());
        String username = jwtUtil.getUserFromToken(cleanToken);

        userService.createUserProfile(createProfile, username);

        ApiResponse userProfile = new ApiResponse<>("User profile created", HttpStatus.OK.value(),
                createProfile, request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('admin', 'mod', 'user')")
    @PutMapping("/profile/updateprofile")
    private ResponseEntity<ApiResponse> updateUserProfile(@RequestBody UserProfileDto updateProfile, WebRequest request) {

        String jwtToken = request.getHeader("Authorization");

        if (jwtToken == null || !jwtToken.startsWith(BEARER_)) {
            throw new RuntimeException("Action not allowed");
        }
        String cleanToken = jwtToken.substring(BEARER_.length());
        String username = jwtUtil.getUserFromToken(cleanToken);

        userService.updateUserProfile(updateProfile, username);

        ApiResponse<UserProfileDto> userProfile = new ApiResponse<>("User profile updated", HttpStatus.OK.value(),
                updateProfile, request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }


    @GetMapping("/profile/{uuid}")
    public UserProfileDto userProfile(@PathVariable String uuid) {
        return userService.getUserProfile(uuid);
    }
}
