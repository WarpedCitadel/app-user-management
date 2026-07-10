package com.warpedcitadel.appusermanagement.management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.accept.ApiVersionStrategy;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ManagementControllerTest {

    @Mock
    private MockMvc mockMvc;

    @Mock
    private ApiVersionStrategy apiVersionStrategy;

    @Mock
    private ManagementService managementService;

    @InjectMocks
    private ManagementController managementController;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(managementController)
                .setApiVersionStrategy(apiVersionStrategy)
                .build();
    }


    @Test
    void _test_getAppUsers() {
    }

    @Test
    void _test_disableAppUser() throws Exception {

        String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";

        mockMvc.perform(put("/api/user/profile/{uuid}/disable", validUUID)
                        .header("x-api-version", "1.0"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.title").value("User status changed"))
                        .andExpect(jsonPath("$.status").value(200))
                        .andExpect(jsonPath("$.data").value("User status set to disabled"))
                        .andExpect(jsonPath("$.instance").value("/api/user/profile/019ea371-9498-7cb1-b4b9-4ee3db8dc132/disable"))
                        .andExpect(jsonPath("$.timestamp").exists());

        Mockito.verify(managementService, Mockito.atLeast(1)).disableAppUser(validUUID);
    }

    @Test
    void _test_enableAppUser() throws Exception {

        String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";

        mockMvc.perform(put("/api/user/profile/{uuid}/enable", validUUID)
                        .header("x-api-version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("User status changed"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").value("User status set to enabled"))
                .andExpect(jsonPath("$.instance").value("/api/user/profile/019ea371-9498-7cb1-b4b9-4ee3db8dc132/enable"))
                .andExpect(jsonPath("$.timestamp").exists());

        Mockito.verify(managementService, Mockito.atLeast(1)).enableAppUser(validUUID);
    }
}