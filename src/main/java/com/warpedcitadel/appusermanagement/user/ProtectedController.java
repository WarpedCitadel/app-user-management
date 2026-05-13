package com.warpedcitadel.appusermanagement.user;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/protected")
public class ProtectedController {

    @GetMapping("/test")
    public String userAccess(){
        return "User content with JWT";
    }
}
