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
    public ResponseEntity<ApiResponse<GetAppUsersDto>> getAppUsers(SearchAttributesDto attributes,
                                   Pageable pageable, WebRequest request) {

        GetAppUsersDto data = managementService.getAppUsers(pageable, attributes);

        ApiResponse<GetAppUsersDto> response = new ApiResponse<>("Get user profiles",
                HttpStatus.OK.value(),
                data,
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/profile/{uuid}/disable")
    public ResponseEntity<ApiResponse> disableAppUser(@PathVariable String uuid, WebRequest request) {
        managementService.disableAppUser(uuid);
        ApiResponse response = new ApiResponse<>("User status changed",
                HttpStatus.OK.value(),
                "User status set to disabled",
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/profile/{uuid}/enable")
    public ResponseEntity<ApiResponse> enableAppUser(@PathVariable String uuid, WebRequest request) {
        managementService.enableAppUser(uuid);
        ApiResponse enableAppUser = new ApiResponse<>("User status changed",
                HttpStatus.OK.value(),
                "User status set to enabled",
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(enableAppUser, HttpStatus.OK);
    }
}
