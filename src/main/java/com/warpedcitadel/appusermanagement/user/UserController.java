package com.warpedcitadel.appusermanagement.user;


import com.warpedcitadel.appusermanagement.user.profile.AppUserProfileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/api/v1/user/profile")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAnyRole('admin', 'mod', 'user')")
    @GetMapping("/{uuid}")
    public AppUserProfileModel userProfile(@PathVariable String uuid){
        return userRepository.getAppUserProfile(uuid);
    }


    // Todo | Make more personalized messages for api request and response
    @PreAuthorize("hasAnyRole('admin', 'mod', 'user')")
    @PostMapping("/createprofile")
    public String userCreateProfile(@RequestBody AppUserProfileModel updateProfile, WebRequest request){
        if (userService.createUserProfile(updateProfile) == 1) {
            return "profile Created!";
        }
        return "Failed to create user bio!";
    }


    // Todo | Make more personalized messages for api request and response
    @PreAuthorize("hasAnyRole('admin', 'mod', 'user')")
    @PostMapping("/updateprofile")
    public String userUpdateProfile(@RequestBody AppUserProfileModel updateProfile, WebRequest request){
        if (userService.updateUserProfile(updateProfile) == 1) {
            return "profile updated!";
        }
        return "Failed to update user bio!";
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
