package com.warpedcitadel.appusermanagement.auth;

import com.warpedcitadel.appusermanagement.auth.model.AuthModel;
import com.warpedcitadel.appusermanagement.auth.model.UserModel;
import com.warpedcitadel.appusermanagement.util.SQLFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class AuthRepository {

    @Autowired
    private DataSource database;

    SQLFileReader loadSQL = new SQLFileReader();


    public AuthModel authenticateUser(String username) {

        String selectSql = loadSQL.loadSQL("/users/select--get_app_user_details.sql");

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSql)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                AuthModel dbUser = new AuthModel(
                        resultSet.getString("user_uuid"),
                        resultSet.getString("username"),
                        resultSet.getString("password_hash"),
                        resultSet.getString("role_type"),
                        resultSet.getBoolean("isactive")
                );
                return dbUser;
            } else {
                throw new IllegalArgumentException("Invalid username or password");
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to authenticate user");
        }
    }


    public void createAppUser(UserModel user) {

        String insertSql = loadSQL.loadSQL("/users/insert--create_app_user.sql");

        try (Connection connection = database.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

            insertStatement.setString(1, user.getUsername());
            insertStatement.setString(2, user.getPasswordHash());
            insertStatement.setString(3, user.getEmail());
            insertStatement.setString(4, user.getToken());
            insertStatement.setString(5, user.getPasscode());

            insertStatement.executeQuery();

        } catch (SQLException exception) {
            throw new RuntimeException("Username or email already exists");
        }
    }


//    public void emailVerificationToken(UserModel user) {
//
//        String insertSql = loadSQL.loadSQL("/users/**");
//
//        try (Connection connection = database.getConnection();
//             PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
//
//            insertStatement.setLong(1, user.getAppUserId());
//            insertStatement.setString(2, user.getToken());
//            insertStatement.setString(3, user.getPasscode());
//
//            insertStatement.execute();
//
//        } catch (SQLException exception) {
//            throw new RuntimeException("Failed to generate user token");
//        }
//    }
}
