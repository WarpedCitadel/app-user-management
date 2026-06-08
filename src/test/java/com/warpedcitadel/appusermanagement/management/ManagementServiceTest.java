package com.warpedcitadel.appusermanagement.management;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ManagementServiceTest {

    @Mock
    private ManagementRepository managementRepository;

    @InjectMocks
    private ManagementService managementService;

    @Test
    void getAppUsers() {
    }

    @Test
    void enableAppUser() {

        String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";

        managementService.enableAppUser(validUUID);
        Mockito.verify(managementRepository, Mockito.atLeast(1)).enableAppUser(validUUID);
    }

    @Test
    void disableAppUser() {

        String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";

        managementService.disableAppUser(validUUID);
        Mockito.verify(managementRepository, Mockito.atLeast(1)).disableAppUser(validUUID);
    }
}