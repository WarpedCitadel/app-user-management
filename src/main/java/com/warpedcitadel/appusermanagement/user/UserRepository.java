package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.util.SQLFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
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

    public boolean authenticateUser(UserModel user){

        String sqlScript = loadSQL.loadSQL("/users/select--get_app_username.sql");

        try (Connection connection = wcDatabase.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlScript)) {

            statement.setString(1, user.getUsername());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedHash = resultSet.getString("password_hash");

                if (BCrypt.checkpw(user.getPasswordHash(), storedHash)) {
                    System.out.println("Login Successful -- JDBC");
                    return true;
                } else {
                    System.out.println("Login Failed -- JDBC");
                    return false;
                }
            } else {
                System.out.println("User not found -- JDBC");
                return false;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
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
