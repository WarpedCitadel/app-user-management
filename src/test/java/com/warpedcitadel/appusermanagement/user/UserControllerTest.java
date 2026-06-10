package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.security.JwtUtil;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private MockMvc mockMvc;

    @Mock
    private ApiVersionStrategy apiVersionStrategy;

    @Mock
    private UserService userService;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    private UserProfileDto profile;
    private List<GameProfileModel> list;
    private GameProfileModel gameProfileModel;
    private final String jwtToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huQmxhbmNoZTEiLCJpYXQiOjE3ODA3Nzk2MDgsImV4cCI6MTc4MDc4MDUwOH0.JrksQumyiVuI68qjMxPcBIOxF6en6DQeYha1cwUZD_U";
    private final String testUsername = "JohnBlanche";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setApiVersionStrategy(apiVersionStrategy)
                .build();

        // Todo: Create a list of games
        list = new ArrayList<>();

        gameProfileModel = new GameProfileModel(
                "019ea2f1-be01-72ca-bf9c-bbf41a925d3d",
                "Warhammer 40k",
                "019ea2f1-c3cd-75a6-97ce-1e4b45182485",
                "It is not the descent towards the darkness, " +
                        "nor the rise to the light that makes us superior. " +
                        "It is the endless struggle between the two that greatness of character lies.",
                "Role playing"
        );

        list.add(gameProfileModel);

         profile = new UserProfileDto(
                "019ea371-9498-7cb1-b4b9-4ee3db8dc132",
                "019e9e91-d2de-70f0-ae45-ebb1c52f557e",
                "JohnBlanche",
                "I'm painfully aware that I'll probably not survive what I want to achieve. " +
                        "I find if I do one thing, it generates four, five or six other things in my imagination and if I do any of those," +
                        " they also generate the same again.",
                list
         );
    }


    @Test
    void _test_createUserProfile() throws Exception {

        Path filePath = Path.of("src/test/resources/json/createUserProfile.json");
        String json = Files.readString(filePath);

        mockMvc.perform(post("/user/profile/createprofile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken)
                        .header("x-api-version", "1.0")
                        .content(json))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value(200))
                        .andExpect(jsonPath("$.title").value("User profile created"))
                        .andExpect(jsonPath("$.data.userUUID").value("019ea371-9498-7cb1-b4b9-4ee3db8dc132"))
                        .andExpect(jsonPath("$.data.displayName").value(testUsername))
                        .andExpect(jsonPath("$.data.biography").value("I'm painfully aware that I'll probably not survive what I want to achieve. " +
                                                        "I find if I do one thing, it generates four, five or six other things in my imagination and if I do any of those," +
                                                        " they also generate the same again."))
                        .andExpect(jsonPath("$.instance").value("/user/profile/createprofile"));
    }


    @Test
    void _test_updateUserProfile() throws Exception {

        Path filePath = Path.of("src/test/resources/json/createUserProfile.json");
        String json = Files.readString(filePath);

        mockMvc.perform(put("/user/profile/updateprofile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken)
                        .header("x-api-version", "1.0")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.title").value("User profile updated"))
                .andExpect(jsonPath("$.data.userUUID").value("019ea371-9498-7cb1-b4b9-4ee3db8dc132"))
                .andExpect(jsonPath("$.data.displayName").value(testUsername))
                .andExpect(jsonPath("$.data.biography").value("I'm painfully aware that I'll probably not survive what I want to achieve. " +
                        "I find if I do one thing, it generates four, five or six other things in my imagination and if I do any of those," +
                        " they also generate the same again."))
                .andExpect(jsonPath("$.instance").value("/user/profile/updateprofile"));
    }


    @Test
    void _test_userProfile() throws Exception {

        String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";

        when(userService.getUserProfile(validUUID)).thenReturn(profile);

        mockMvc.perform(get("/user/profile/{uuid}", validUUID)
                        .header("x-api-version", "1.0"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.data.userUUID").value("019ea371-9498-7cb1-b4b9-4ee3db8dc132"))
                        .andExpect(jsonPath("$.data.profileImgUUID").value("019e9e91-d2de-70f0-ae45-ebb1c52f557e"))
                        .andExpect(jsonPath("$.data.displayName").value(testUsername))
                        .andExpect(jsonPath("$.data.biography").value("I'm painfully aware that I'll probably not survive what I want to achieve. " +
                                                                    "I find if I do one thing, it generates four, five or six other things in my imagination and if I do any of those," +
                                                                    " they also generate the same again."))
                        .andExpect(jsonPath("$.data.createdGames").exists())
                        .andExpect(jsonPath("$.data.createdGames[0].fileUUID").value(gameProfileModel.getFileUUID()))
                        .andExpect(jsonPath("$.data.createdGames[0].title").value(gameProfileModel.getTitle()))
                        .andExpect(jsonPath("$.data.createdGames[0].coverImgUUID").value(gameProfileModel.getCoverImgUUID()))
                        .andExpect(jsonPath("$.data.createdGames[0].description").value(gameProfileModel.getDescription()))
                        .andExpect(jsonPath("$.data.createdGames[0].genre").value(gameProfileModel.getGenre()));

        Mockito.verify(userService, Mockito.atLeast(1)).getUserProfile(validUUID);
    }
}
