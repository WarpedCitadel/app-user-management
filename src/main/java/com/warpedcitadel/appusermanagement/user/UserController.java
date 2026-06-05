package com.warpedcitadel.appusermanagement.user;


import com.warpedcitadel.appusermanagement.payload.ApiResponse;
import com.warpedcitadel.appusermanagement.security.JwtUtil;
import com.warpedcitadel.appusermanagement.user.model.AppUserProfileModel;
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
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;


    @PreAuthorize("hasAnyRole('admin', 'mod', 'user')")
    @PostMapping("/profile/createprofile")
    private ResponseEntity<ApiResponse> createUserProfile(@RequestBody AppUserProfileModel updateProfile, WebRequest request) {
        if(userService.createUserProfile(updateProfile) >= 1){
            ApiResponse userProfile = new ApiResponse<>("User profile created", HttpStatus.OK.value(),
                    updateProfile, request.getDescription(false).replace("uri=", ""), Instant.now(Clock.systemUTC()));
            return new ResponseEntity<>(userProfile, HttpStatus.OK);
        } else {
            ApiResponse failedUpdate = new ApiResponse<>("Bad request", HttpStatus.BAD_REQUEST.value(),
                    "Failed to update user profile", request.getDescription(false).replace("uri=", ""), Instant.now(Clock.systemUTC()));
            return new ResponseEntity<>(failedUpdate, HttpStatus.BAD_REQUEST);
        }
    }


    @PreAuthorize("hasAnyRole('admin', 'mod', 'user')")
    @PutMapping("/profile/updateprofile")
    private ResponseEntity<ApiResponse> updateUserProfile(@RequestBody AppUserProfileModel updateProfile, WebRequest request) {

        String jwtToken = request.getHeader("Authorization");

        if (jwtToken == null || jwtToken.startsWith(BEARER_)) {
            String cleanToken = jwtToken.substring(BEARER_.length());
            String username = jwtUtil.getUserFromToken(cleanToken);
            if (userService.updateUserProfile(updateProfile, username)) {
                ApiResponse userProfile = new ApiResponse<>("User profile updated", HttpStatus.OK.value(),
                        updateProfile, request.getDescription(false).replace("uri=", ""),
                        Instant.now(Clock.systemUTC()));
                return new ResponseEntity<>(userProfile, HttpStatus.OK);
            }
        }
        ApiResponse failedUpdate = new ApiResponse<>("Bad request", HttpStatus.BAD_REQUEST.value(),
                "Failed to update user profile", request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(failedUpdate, HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/profile/{uuid}")
    public AppUserProfileModel userProfile(@PathVariable String uuid) {
        return userRepository.getAppUserProfile(uuid);
    }
}
