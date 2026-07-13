package com.warpedcitadel.appusermanagement.user;


import com.warpedcitadel.appusermanagement.payload.ApiResponse;
import com.warpedcitadel.appusermanagement.user.dto.UserProfileDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.Instant;

@RestController
@RequestMapping(path = "/api/user", version = "1.0")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

    @GetMapping("/profile/{uuid}")
    public ResponseEntity<ApiResponse> userProfile(@PathVariable String uuid, WebRequest request) {

        UserProfileDto profile = userService.getUserProfile(uuid);

        ApiResponse<UserProfileDto> userProfile = new ApiResponse<>("User profile", HttpStatus.OK.value(),
                profile, request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }
}
