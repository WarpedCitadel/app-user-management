package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.security.AuthenticationModel;
import com.warpedcitadel.appusermanagement.util.SQLFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class UserRepository {

    @Autowired
    private DataSource wcDatabase;

    SQLFileReader loadSQL = new SQLFileReader();

    // TODO | authenticate and register needs more robust queries
    // Possibly ask for email verification later on

    public AuthenticationModel authenticateUser(String username){

        String sqlScript = loadSQL.loadSQL("/users/select--get_app_user_details.sql");

        try (Connection connection = wcDatabase.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlScript)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                AuthenticationModel dbUser = new AuthenticationModel(
                        resultSet.getString("uuid"),
                        resultSet.getString("username"),
                        resultSet.getString("password_hash"),
                        resultSet.getString("role_type")
                );
                return dbUser;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        throw new RuntimeException("User not found! -- JDBC");
    }

//    Todo : user exist conception needs to return a api response
    public int registerUser(UserModel user) {

        String insertSql = loadSQL.loadSQL("/users/insert--create_app_user.sql");

        try (Connection connection = wcDatabase.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

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

        } catch (SQLIntegrityConstraintViolationException userExistException) {
            throw new RuntimeException("Username or email already exists!", userExistException);
        } catch (SQLException genericException) {
            throw new RuntimeException("Database error", genericException);
        }
    }
}
