package com.warpedcitadel.appusermanagement.audit;

import com.warpedcitadel.appusermanagement.audit.dto.UserSessionsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.accept.ApiVersionStrategy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuditControllerTest {

    @Mock
    private MockMvc mockMvc;

    @Mock
    private ApiVersionStrategy apiVersionStrategy;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private AuditController auditController;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(auditController)
                .setApiVersionStrategy(apiVersionStrategy)
                .build();
    }


    @Test
    void _test_getUserSessions() throws Exception {

        Path filePath = Path.of("src/test/resources/json/appUserSessions.json");
        String validJson = Files.readString(filePath);

        List<String> sessions = List.of("2026-03-12 23:17:37.290907",
                                        "2026-03-17 10:43:27.290907",
                                        "2026-05-01 06:34:17.290907",
                                        "2026-06-23 14:45:37.290907",
                                        "2026-07-12 01:54:17.290907",
                                        "2026-06-06 08:12:57.290907");

        String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";
        UserSessionsDto userSessions = new UserSessionsDto(sessions);

        when(auditService.getAppUserSessions(validUUID)).thenReturn(userSessions.sessions());

        mockMvc.perform(get("/user/profile/{uuid}/session", validUUID)
                .header("x-api-version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json(validJson));
    }
}