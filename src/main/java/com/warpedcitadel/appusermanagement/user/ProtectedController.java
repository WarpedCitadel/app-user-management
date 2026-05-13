package com.warpedcitadel.appusermanagement.user;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/protected")
public class ProtectedController {

    /*
    This Controller Class is only temporary. For testing JWT Token access for each role such as
    user, mod, and admin. Unauthenticated users should not have access to the data that resides here.
    */
    @PreAuthorize("hasAnyRole('admin', 'mod', 'user')")
    @GetMapping("/user")
    public String userAccess(){
        return "User content with JWT";
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
