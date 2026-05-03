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

    // TODO | Login needs more robust queries

    public boolean authenticateUser(String username, String passwordHash){

        String sqlScript = loadSQL.loadSQL("/users/select--get_app_username.sql");

        try (Connection connection = wcDatabase.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlScript)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedHash = resultSet.getString("password_hash");

                if (BCrypt.checkpw(passwordHash, storedHash)) {
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

    public int registerUser(UserModel user) {

        String sqlScript = loadSQL.loadSQL("/users/insert--create_app_user.sql");

        try (Connection connection = wcDatabase.getConnection();
        PreparedStatement statement = connection.prepareStatement(sqlScript, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getEmail());

            int affected = statement.executeUpdate();

            if (affected == 1) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) return resultSet.getInt(1);
                }
            }

            return -1;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return -1;
        }
    }
}
