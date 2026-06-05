package com.warpedcitadel.appusermanagement.auth;

import com.warpedcitadel.appusermanagement.user.model.UserModel;
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

        String sqlScript = loadSQL.loadSQL("/users/select--get_app_user_details.sql");

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlScript)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                AuthModel dbUser = new AuthModel(
                        resultSet.getString("user_uuid"),
                        resultSet.getString("username"),
                        resultSet.getString("password_hash"),
                        resultSet.getString("role_type")
                );
                return dbUser;
            } else {
                throw new IllegalArgumentException("Invalid username or password");
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to authenticate user");
        }
    }


    public int registerUser(UserModel user) {

        String insertSql = loadSQL.loadSQL("/users/insert--create_app_user.sql");

        try (Connection connection = database.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSql,
                     Statement.RETURN_GENERATED_KEYS)) {

            insertStatement.setString(1, user.getUsername());
            insertStatement.setString(2, user.getPasswordHash());
            insertStatement.setString(3, user.getEmail());

            int rowAffected = insertStatement.executeUpdate();

            if (rowAffected == 1) {
                try (ResultSet resultSet = insertStatement.getGeneratedKeys()) {
                    if (resultSet.next()) return resultSet.getInt(1);
                }
            }

            return -1;
        } catch (SQLException exception) {
            throw new RuntimeException("Username or email already exists");
        }
    }
}
