package com.warpedcitadel.appusermanagement.user;


import com.warpedcitadel.appusermanagement.user.profile.AppUserProfileModel;
import com.warpedcitadel.appusermanagement.user.profile.UpdateBioModel;
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


    @PreAuthorize("hasAnyRole('admin', 'mod', 'user')")
    @PatchMapping("/{uuid}")
    public String userUpdateBio(@RequestBody UpdateBioModel updateBio, WebRequest request){
        if (userService.updateUserBio(updateBio) == 1) {
            return "profile updated!";
        }
        return "Failed to update user bio!";
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
