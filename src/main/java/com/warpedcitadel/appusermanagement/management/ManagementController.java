package com.warpedcitadel.appusermanagement.management;

import com.warpedcitadel.appusermanagement.management.dto.GetAppUsersDto;
import com.warpedcitadel.appusermanagement.management.dto.SearchAttributesDto;
import com.warpedcitadel.appusermanagement.payload.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.Instant;


@RestController
@RequestMapping(path = "/user", version = "1.0")
public class ManagementController {

    @Autowired
    private ManagementService managementService;


    @GetMapping("/search")
    public GetAppUsersDto getAppUsers(SearchAttributesDto attributes,
                                   Pageable pageable) {
        return managementService.getAppUsers(pageable, attributes);
    }


    @PutMapping("/profile/{uuid}/disable")
    public ResponseEntity<ApiResponse> disableAppUser(@PathVariable String uuid, WebRequest request) {
        managementService.disableAppUser(uuid);
        ApiResponse disableAppUser = new ApiResponse<>("Status Changed", HttpStatus.OK.value(),
                "User status changed to disabled", request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(disableAppUser, HttpStatus.OK);
    }


    @PutMapping("/profile/{uuid}/enable")
    public ResponseEntity<ApiResponse> enableAppUser(@PathVariable String uuid, WebRequest request) {
        managementService.enableAppUser(uuid);
        ApiResponse enableAppUser = new ApiResponse<>("Status Changed", HttpStatus.OK.value(),
                "User status changed to enabled", request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(enableAppUser, HttpStatus.OK);
    }
}
