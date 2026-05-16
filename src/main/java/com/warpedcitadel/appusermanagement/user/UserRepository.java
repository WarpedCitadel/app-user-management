package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.security.AuthenticationModel;
import com.warpedcitadel.appusermanagement.user.profile.AppUserProfileModel;
import com.warpedcitadel.appusermanagement.user.profile.UpdateBioModel;
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

    public AuthenticationModel authenticateUser(String username) {

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

        } catch (SQLException userExistException) {
            throw new RuntimeException("Username or email already exists!", userExistException);
        }
    }


    public AppUserProfileModel getAppUserProfile(String uuid){

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_user_profile.sql");


        try (Connection connection = wcDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {

            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                AppUserProfileModel userProfile = new AppUserProfileModel(
                        resultSet.getString("uuid"),
                        resultSet.getString("username"),
                        resultSet.getString("user_bio")
                );
                return userProfile;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        throw new RuntimeException("User profile not found! -- JDBC");
    }


    public int updateDbBio(UpdateBioModel updateBio){

        String updateSql = loadSQL.loadSQL("/users/insert--create_user_bio.sql");

        try (Connection connection = wcDatabase.getConnection();
            PreparedStatement updateStatement = connection.prepareStatement(updateSql, Statement.RETURN_GENERATED_KEYS)) {

            updateStatement.setInt(1, updateBio.getAppUserId());
            updateStatement.setString(2, updateBio.getBio());

            int rowAffected = updateStatement.executeUpdate();

            if (rowAffected == 1){
                try (ResultSet resultSet = updateStatement.getGeneratedKeys()) {
                    if (resultSet.next()) return resultSet.getInt(1);
                }
            }

            return -1;

        } catch (SQLException updateException){
            throw new RuntimeException("Failed to update user bio!", updateException);
        }

    }


    // ################################### Helper Functions #########################################

    public int getUserId(String uuid){

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_user_id.sql");

        try (Connection connection = wcDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {

            statement.setString(1, uuid);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        throw new RuntimeException("User does not exist!");
    }
}
