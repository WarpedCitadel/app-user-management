package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.user.model.AppUserProfileModel;
import com.warpedcitadel.appusermanagement.user.model.GameProfileModel;
import com.warpedcitadel.appusermanagement.util.SQLFileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    @Spy
    private UserRepository userRepository;

    private SQLFileReader loadSQL = new SQLFileReader();

    private AppUserProfileModel appUserProfileModel;

    private GameProfileModel gameProfileModel;

    private List<GameProfileModel> gamesList;

    @BeforeEach
    void setup() {

        appUserProfileModel = new AppUserProfileModel(
                1L,
                "019ea371-9498-7cb1-b4b9-4ee3db8dc132",
                "JohnBlanche",
                "Discipline. Duty. Unyielding Will. These judge every warrior."
        );

        gameProfileModel = new GameProfileModel(
                "019ea2f1-be01-72ca-bf9c-bbf41a925d3d",
                "Warhammer 40k",
                "019ea2f1-c3cd-75a6-97ce-1e4b45182485",
                "It is not the descent towards the darkness, " +
                        "nor the rise to the light that makes us superior. " +
                        "It is the endless struggle between the two that greatness of character lies.",
                "Role playing"
        );

        gamesList = new ArrayList<>();
        gamesList.add(gameProfileModel);
    }


    @Test
    void _test_getAppUserProfile() throws SQLException {

        String selectSql = loadSQL.loadSQL("/users/select--get_app_user_profile.sql");
        String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";

        doReturn(gamesList).when(userRepository).getUserGames(validUUID);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(selectSql)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true, false);

        when(resultSet.getString("user_uuid"))
                .thenReturn("019ea371-9498-7cb1-b4b9-4ee3db8dc132");

        when(resultSet.getString("display_name"))
                .thenReturn("Eliphas");

        when(resultSet.getString("user_bio"))
                .thenReturn("Discipline. Duty. Unyielding Will. These judge every warrior.");

        when(resultSet.getString("img_uuid"))
                .thenReturn("019ea2f1-c3cd-75a6-97ce-1e4b45182485");

         appUserProfileModel = userRepository.getAppUserProfile(validUUID);

        verify(preparedStatement).setString(1, validUUID);
        verify(userRepository, times(1)).getUserGames(validUUID);
        verify(preparedStatement).executeQuery();

        Assertions.assertEquals("019ea371-9498-7cb1-b4b9-4ee3db8dc132", appUserProfileModel.getUuid());
        Assertions.assertEquals("Eliphas", appUserProfileModel.getDisplayName());
        Assertions.assertEquals("Discipline. Duty. Unyielding Will. These judge every warrior.", appUserProfileModel.getBio());
        Assertions.assertEquals("019ea2f1-c3cd-75a6-97ce-1e4b45182485", appUserProfileModel.getProfileIMG());
        Assertions.assertEquals(gamesList, appUserProfileModel.getCreatedGames());
    }


    @Test
    void _test_createAppUserProfile() throws SQLException {

        String insertSql = loadSQL.loadSQL("/users/insert--create_app_user_profile.sql");

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        userRepository.createAppUserProfile(appUserProfileModel);

        verify(preparedStatement).setLong(1, appUserProfileModel.getAppUserId());
        verify(preparedStatement).setString(2, appUserProfileModel.getDisplayName());
        verify(preparedStatement).setString(3, appUserProfileModel.getBio());
        verify(preparedStatement).getGeneratedKeys();
        verify(preparedStatement).executeUpdate();
    }


    @Test
    void _test_updateAppUserProfile() throws SQLException {

        String updateSql = loadSQL.loadSQL("/users/update--update_app_user_profile.sql");

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(updateSql, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        userRepository.updateAppUserProfile(appUserProfileModel);

        verify(preparedStatement).setString(1, appUserProfileModel.getDisplayName());
        verify(preparedStatement).setString(2, appUserProfileModel.getBio());
        verify(preparedStatement).setLong(3, appUserProfileModel.getAppUserId());
        verify(preparedStatement).getGeneratedKeys();
        verify(preparedStatement).executeUpdate();
    }


    @Test
    void _test_getUserIdByUuid() throws SQLException {

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_user_id.sql");
        String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(selectSQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true, false);

        when(resultSet.getLong("id"))
                .thenReturn(1L);

         long result = userRepository.getUserIdByUuid(validUUID);

        verify(preparedStatement).setString(1, validUUID);
        verify(preparedStatement).executeQuery();
        Assertions.assertEquals(1L, result);
    }


    @Test
    void _test_getUserIdByUsername() throws SQLException {

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_user_id_u.sql");
        String username = "JohnBlanche";

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(selectSQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true, false);

        when(resultSet.getLong("id"))
                .thenReturn(1L);

        long result = userRepository.getUserIdByUsername(username);

        verify(preparedStatement).setString(1, username);
        verify(preparedStatement).executeQuery();
        Assertions.assertEquals(1L, result);
    }


    @Test
    void _test_getUserGames() throws SQLException {

        String selectSQL = loadSQL.loadSQL("/users/select--get_game_profiles.sql");
        String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(selectSQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true, false);

        when(resultSet.getString(1))
                .thenReturn("019ea2f1-be01-72ca-bf9c-bbf41a925d3d");

        when(resultSet.getString("file_uuid"))
                .thenReturn(gameProfileModel.getFileUUID());

        when(resultSet.getString("title"))
                .thenReturn(gameProfileModel.getTitle());

        when(resultSet.getString("img_uuid"))
                .thenReturn(gameProfileModel.getCoverImgUUID());

        when(resultSet.getString("short_desc"))
                .thenReturn(gameProfileModel.getDescription());

        when(resultSet.getString("genre_type"))
                .thenReturn(gameProfileModel.getGenre());

        gamesList = userRepository.getUserGames(validUUID);

        verify(preparedStatement).setString(1, validUUID);
        verify(preparedStatement).executeQuery();
    }
}
