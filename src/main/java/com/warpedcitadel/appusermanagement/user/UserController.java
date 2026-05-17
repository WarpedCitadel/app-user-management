package com.warpedcitadel.appusermanagement.user;


import com.warpedcitadel.appusermanagement.payload.ApiResponse;
import com.warpedcitadel.appusermanagement.user.profile.AppUserProfileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.Instant;

@RestController
@RequestMapping("/api/v1/user/profile")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/{uuid}")
    public AppUserProfileModel userProfile(@PathVariable String uuid){
        return userRepository.getAppUserProfile(uuid);
    }


    @PreAuthorize("hasAnyRole('admin', 'mod', 'user')")
    @PostMapping("/createprofile")
    private ResponseEntity<ApiResponse> createUserProfile(@RequestBody AppUserProfileModel updateProfile, WebRequest request){
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
    @PutMapping("/updateprofile")
    private ResponseEntity<ApiResponse> updateUserProfile(@RequestBody AppUserProfileModel updateProfile, WebRequest request) {
        if(userService.updateUserProfile(updateProfile) >= 1){
            ApiResponse userProfile = new ApiResponse<>("User profile created", HttpStatus.OK.value(),
                    updateProfile, request.getDescription(false).replace("uri=", ""), Instant.now(Clock.systemUTC()));
            return new ResponseEntity<>(userProfile, HttpStatus.OK);
        } else {
            ApiResponse failedUpdate = new ApiResponse<>("Bad request", HttpStatus.BAD_REQUEST.value(),
                    "Failed to update user profile", request.getDescription(false).replace("uri=", ""), Instant.now(Clock.systemUTC()));
            return new ResponseEntity<>(failedUpdate, HttpStatus.BAD_REQUEST);
        }
    }


//  Todo | Make user management system via admin and mod roles
    @PreAuthorize("hasAnyRole('admin', 'mod')")
    @GetMapping("/mod")
    public String modAccess(){
        return "Mod content with JWT";
    }

    @PreAuthorize("(hasRole('admin'))")
    @GetMapping("/admin")
    public String adminAccess(){
        return "Admin content with JWT";
    }
}
