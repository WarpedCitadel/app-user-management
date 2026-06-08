package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.user.dto.UserProfileDto;
import com.warpedcitadel.appusermanagement.user.model.GameProfileModel;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private MockMvc mockMvc;

    @Mock
    private ApiVersionStrategy apiVersionStrategy;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setApiVersionStrategy(apiVersionStrategy)
                .build();
    }


    @Test
    void _test_createUserProfile() {

    }


    @Test
    void _test_updateUserProfile() {

    }


    @Test
    void _test_userProfile() throws Exception {

        String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";

        // Todo: Create a list of games
        List<GameProfileModel> list = new ArrayList<>();

        UserProfileDto profile = new UserProfileDto(
                "019ea371-9498-7cb1-b4b9-4ee3db8dc132",
                "019e9e91-d2de-70f0-ae45-ebb1c52f557e",
                "JohnBlanche",
                "I'm painfully aware that I'll probably not survive what I want to achieve. " +
                        "I find if I do one thing, it generates four, five or six other things in my imagination and if I do any of those," +
                        " they also generate the same again.",
                list
        );

        when(userService.getUserProfile(validUUID)).thenReturn(profile);

        mockMvc.perform(get("/user/profile/{uuid}", validUUID)
                        .header("x-api-version", "1.0"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.userUUID").value("019ea371-9498-7cb1-b4b9-4ee3db8dc132"))
                        .andExpect(jsonPath("$.profileImgUUID").value("019e9e91-d2de-70f0-ae45-ebb1c52f557e"))
                        .andExpect(jsonPath("$.displayName").value("JohnBlanche"))
                        .andExpect(jsonPath("$.biography").value("I'm painfully aware that I'll probably not survive what I want to achieve. " +
                                                                    "I find if I do one thing, it generates four, five or six other things in my imagination and if I do any of those," +
                                                                    " they also generate the same again."))
                        .andExpect(jsonPath("$.createdGames").value(list));

        Mockito.verify(userService, Mockito.atLeast(1)).getUserProfile(validUUID);
    }
}
