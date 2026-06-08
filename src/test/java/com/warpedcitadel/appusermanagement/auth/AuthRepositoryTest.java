package com.warpedcitadel.appusermanagement.auth;

import com.warpedcitadel.appusermanagement.auth.model.AuthModel;
import com.warpedcitadel.appusermanagement.auth.model.UserModel;
import com.warpedcitadel.appusermanagement.util.SQLFileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthRepositoryTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private SQLFileReader loadSQL = new SQLFileReader();

    @InjectMocks
    private AuthRepository authRepository;


    @Test
    void _test_createAppUser() throws SQLException {

        String insertSql = loadSQL.loadSQL("/users/insert--create_app_user.sql");

        UserModel userModel = new UserModel(
                "JohnBlanche",
                "$2a$10$8Hdtn/Ih2Pjd1V5780RVHe8NOLnZZFdjOyk1kax8CpDFHInsDG7A6",
                "johnblanche@gmail.com");

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(insertSql)).thenReturn(preparedStatement);

        authRepository.createAppUser(userModel);

        verify(preparedStatement).setString(1, userModel.getUsername());
        verify(preparedStatement).setString(2, userModel.getPasswordHash());
        verify(preparedStatement).setString(3, userModel.getEmail());
        verify(preparedStatement).execute();
    }


    @Test
    void _test_authenticateUser() throws SQLException {

        String selectSql = loadSQL.loadSQL("/users/select--get_app_user_details.sql");

        UserModel userModel = new UserModel(
                "JohnBlanche"
        );

        AuthModel validUser = new AuthModel(
                "019ea371-9498-7cb1-b4b9-4ee3db8dc132",
                "JohnBlanche",
                "$2a$10$8Hdtn/Ih2Pjd1V5780RVHe8NOLnZZFdjOyk1kax8CpDFHInsDG7A6",
                "user"
        );

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(selectSql)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true, false);
        when(resultSet.getString("user_uuid"))
                .thenReturn("019ea371-9498-7cb1-b4b9-4ee3db8dc132");
        when(resultSet.getString("username"))
                .thenReturn("JohnBlanche");
        when(resultSet.getString("password_hash"))
                .thenReturn("$2a$10$8Hdtn/Ih2Pjd1V5780RVHe8NOLnZZFdjOyk1kax8CpDFHInsDG7A6");
        when(resultSet.getString("role_type"))
                .thenReturn("user");

        AuthModel mockDbUser = authRepository.authenticateUser(userModel.getUsername());

        verify(preparedStatement).setString(1, userModel.getUsername());
        verify(preparedStatement).executeQuery();
        Assertions.assertEquals(validUser.getUuid(), mockDbUser.getUuid());
        Assertions.assertEquals(validUser.getUsername(), mockDbUser.getUsername());
        Assertions.assertEquals(validUser.getPasswordHash(), mockDbUser.getPasswordHash());
        Assertions.assertEquals(validUser.getRole(), mockDbUser.getRole());
    }
}
