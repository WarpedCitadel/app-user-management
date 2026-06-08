package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.user.dto.UserProfileDto;
import com.warpedcitadel.appusermanagement.user.model.AppUserProfileModel;
import com.warpedcitadel.appusermanagement.user.model.GameProfileModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private AppUserProfileModel appUserProfileModel;

    private GameProfileModel gameProfileModel;

    @BeforeEach
    void setup() {

        gameProfileModel = new GameProfileModel(
                "019ea2f1-be01-72ca-bf9c-bbf41a925d3d",
                "Warhammer 40k",
                "019ea2f1-c3cd-75a6-97ce-1e4b45182485",
                "It is not the descent towards the darkness, " +
                        "nor the rise to the light that makes us superior. " +
                        "It is the endless struggle between the two that greatness of character lies.",
                "Role playing"
        );

        List<GameProfileModel> games = new ArrayList();
        games.add(gameProfileModel);

        appUserProfileModel = new AppUserProfileModel(
                "019ea371-9498-7cb1-b4b9-4ee3db8dc132",
                "JohnBlanche",
                "I'm painfully aware that I'll probably not survive what I want to achieve. " +
                        "I find if I do one thing, it generates four, five or six other things in my imagination and if I do any of those," +
                        " they also generate the same again.",
                "019e9e91-d2de-70f0-ae45-ebb1c52f557e",
                games

        );
    }

    @Test
    void _test_getUserProfile() {

        // User profile
        String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";
        when(userRepository.getAppUserProfile(validUUID)).thenReturn(appUserProfileModel);

        UserProfileDto testProfile = userService.getUserProfile(validUUID);


        Assertions.assertNotNull(testProfile);
        Assertions.assertNotNull(testProfile.createdGames());
        Assertions.assertEquals("019ea371-9498-7cb1-b4b9-4ee3db8dc132", testProfile.userUUID());
        Assertions.assertEquals("JohnBlanche", testProfile.displayName());
        Assertions.assertEquals("I'm painfully aware that I'll probably not survive what I want to achieve. " +
                                        "I find if I do one thing, it generates four, five or six other things in my imagination and if I do any of those," +
                                        " they also generate the same again.", testProfile.biography());
        Assertions.assertEquals("019e9e91-d2de-70f0-ae45-ebb1c52f557e", testProfile.profileImgUUID());

        // User games
        GameProfileModel gameResult = testProfile.createdGames().get(0);
        Assertions.assertEquals(1, testProfile.createdGames().size());
        Assertions.assertEquals("019ea2f1-be01-72ca-bf9c-bbf41a925d3d", gameResult.getFileUUID());
        Assertions.assertEquals("Warhammer 40k", gameResult.getTitle());
        Assertions.assertEquals("019ea2f1-c3cd-75a6-97ce-1e4b45182485", gameResult.getCoverImgUUID());
        Assertions.assertEquals("It is not the descent towards the darkness, " +
                                        "nor the rise to the light that makes us superior. " +
                                        "It is the endless struggle between the two that greatness of character lies.", gameResult.getDescription());
        Assertions.assertEquals("Role playing", gameResult.getGenre());
    }
}
