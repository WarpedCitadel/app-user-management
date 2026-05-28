package com.warpedcitadel.appusermanagement.user;


import com.warpedcitadel.appusermanagement.payload.ApiResponse;
import com.warpedcitadel.appusermanagement.security.JwtUtil;
import com.warpedcitadel.appusermanagement.user.profile.AppUserProfileModel;
import com.warpedcitadel.appusermanagement.user.usermanagement.UserAuditModel;
import com.warpedcitadel.appusermanagement.user.usermanagement.UserDetailsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

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
    @PutMapping("/profile/updateprofile")
    private ResponseEntity<ApiResponse> updateUserProfile(@RequestBody AppUserProfileModel updateProfile, WebRequest request) {
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken == null || jwtToken.startsWith(BEARER_)) {
            String cleanToken = jwtToken.substring(BEARER_.length());
            String username = jwtUtil.getUserFromToken(cleanToken);
            if (userService.updateUserProfile(updateProfile, username)) {
                ApiResponse userProfile = new ApiResponse<>("User profile updated", HttpStatus.OK.value(),
                        updateProfile, request.getDescription(false).replace("uri=", ""), Instant.now(Clock.systemUTC()));
                return new ResponseEntity<>(userProfile, HttpStatus.OK);
            }
        }
        ApiResponse failedUpdate = new ApiResponse<>("Bad request", HttpStatus.BAD_REQUEST.value(),
                "Failed to update user profile", request.getDescription(false).replace("uri=", ""), Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(failedUpdate, HttpStatus.BAD_REQUEST);
    }


//  Todo | Make user management system via admin and mod roles

    @GetMapping("/search")
    public Slice<UserDetailsModel> getUsers(@RequestParam("query") String searchterm,
                                            Pageable pageable) {
        return userService.getAppUsers(pageable, searchterm);
    }


    @GetMapping("/profile/{uuid}")
    public AppUserProfileModel userProfile(@PathVariable String uuid) {
        return userRepository.getAppUserProfile(uuid);
    }


    @GetMapping("/profile/{uuid}/session")
    public List<UserAuditModel> getAppUserSessions(@PathVariable String uuid) {
       return userRepository.getAppUserSessions(uuid);
    }

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
