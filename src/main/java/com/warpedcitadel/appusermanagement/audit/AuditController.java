package com.warpedcitadel.appusermanagement.audit;

import com.warpedcitadel.appusermanagement.audit.dto.UserSessionsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user", version = "1.0")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @GetMapping("/profile/{uuid}/session")
    public UserSessionsDto getUserSessions(@PathVariable String uuid) {
       return auditService.getAppUserSessions(uuid);
    }
}
