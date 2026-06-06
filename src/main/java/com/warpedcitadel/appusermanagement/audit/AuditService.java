package com.warpedcitadel.appusermanagement.audit;

import com.warpedcitadel.appusermanagement.audit.dto.UserSessionsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    @Autowired
    private AuditRepository auditRepository;

    public UserSessionsDto getAppUserSessions(String uuid){
        return new UserSessionsDto(auditRepository.getAppUserSessions(uuid));
    }
}
