package com.warpedcitadel.appusermanagement.user;


import com.warpedcitadel.appusermanagement.payload.ApiResponse;
import com.warpedcitadel.appusermanagement.user.dto.UserProfileDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/user", version = "1.0")
public class UserController {


    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @PostMapping("/profile/createprofile")
    private ResponseEntity<ApiResponse> createUserProfile(@RequestBody UserProfileDto createProfile, WebRequest request) {

        userService.createUserProfile(createProfile);

        ApiResponse userProfile = new ApiResponse<>("User profile created", HttpStatus.OK.value(),
                createProfile, request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @PutMapping("/profile/updateprofile")
    private ResponseEntity<ApiResponse> updateUserProfile(@RequestBody UserProfileDto updateProfile, WebRequest request) {

        userService.updateUserProfile(updateProfile);

        ApiResponse<UserProfileDto> userProfile = new ApiResponse<>("User profile updated", HttpStatus.OK.value(),
                updateProfile, request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @GetMapping("/profile/{identifier}")
    public ResponseEntity<ApiResponse> userProfile(@PathVariable String identifier, WebRequest request) {

        String userUUID;

        if (!isValidUUID(identifier)) {
            userUUID = userRepository.getUserIdByUsername(identifier);
            if (userUUID.isBlank()) {
                throw new RuntimeException("Could not retrieve a user id for username: " + identifier);
            }

        } else {
            userUUID = identifier;
        }

        UserProfileDto profile = userService.getUserProfile(userUUID);

        ApiResponse<UserProfileDto> userProfile = new ApiResponse<>("User profile", HttpStatus.OK.value(),
                profile, request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }


    // ### Helper Methods ###

    public boolean isValidUUID(String username) {

        try {
            UUID.fromString(username);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
