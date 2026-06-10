package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.user.model.AppUserProfileModel;
import com.warpedcitadel.appusermanagement.user.model.GameProfileModel;
import com.warpedcitadel.appusermanagement.util.SQLFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private DataSource database;

    SQLFileReader loadSQL = new SQLFileReader();

    public AppUserProfileModel getAppUserProfile(String uuid) {

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_user_profile.sql");

        List<GameProfileModel> gameList = getUserGames(uuid);

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {

            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new AppUserProfileModel(
                        resultSet.getString("user_uuid"),
                        resultSet.getString("display_name"),
                        resultSet.getString("user_bio"),
                        resultSet.getString("img_uuid"),
                        gameList
                );
            } else {
                throw new RuntimeException("Failed to get user profile");
            }
        } catch (SQLException exception) {
            throw new UsernameNotFoundException("Failed to find user with uuid: " + uuid, exception);
        }
    }


    public void createAppUserProfile(AppUserProfileModel updateProfile){

        String insertSql = loadSQL.loadSQL("/users/insert--create_app_user_profile.sql");

        try (Connection connection = database.getConnection();
            PreparedStatement updateStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            updateStatement.setLong(1, updateProfile.getAppUserId());
            updateStatement.setString(2, updateProfile.getDisplayName());
            updateStatement.setString(3, updateProfile.getBio());

            int rowAffected = updateStatement.executeUpdate();

            if (rowAffected == 1){
                updateStatement.getGeneratedKeys();
            }
        } catch (SQLException exception){
            throw new RuntimeException("Failed to create user profile with uuid: " + updateProfile.getUuid(), exception);
        }
    }


    public void updateAppUserProfile(AppUserProfileModel updateProfile){

        String updateSql = loadSQL.loadSQL("/users/update--update_app_user_profile.sql");

        try (Connection connection = database.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateSql, Statement.RETURN_GENERATED_KEYS)) {

            updateStatement.setString(1, updateProfile.getDisplayName());
            updateStatement.setString(2, updateProfile.getBio());
            updateStatement.setLong(3, updateProfile.getAppUserId());

            int rowAffected = updateStatement.executeUpdate();

            if (rowAffected == 1) {
                 updateStatement.getGeneratedKeys();
            }
        } catch (SQLException updateException){
            throw new RuntimeException("Failed to update user profile with uuid: " +
                    updateProfile.getUuid(), updateException);
        }
    }

    // ################################### Helper Functions #########################################

    public long getUserIdByUuid(String uuid){

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_user_id.sql");

        try (Connection connection = database.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {

            selectStatement.setString(1, uuid);

            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
        } catch (SQLException exception) {
            throw new RuntimeException("User with uuid: " + uuid + " does not exist", exception);
        }
        return -1;
    }


    public long getUserIdByUsername(String username) {

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_user_id_u.sql");

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
        } catch (SQLException exception) {
            throw new UsernameNotFoundException("User with the username of " + username + " does not exist");
        }
        return -1;
    }


    public List<GameProfileModel> getUserGames(String uuid) {

            String selectSQL = loadSQL.loadSQL("/users/select--get_game_profiles.sql");

            List<GameProfileModel> games = new ArrayList<>();

            try (Connection connection = database.getConnection();
            PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {

                selectStatement.setString(1, uuid);

                ResultSet resultSet = selectStatement.executeQuery();

                while (resultSet.next()) {

                    String fileUUID = resultSet.getString(1);
                    if (fileUUID == null) {
                        continue;
                    }
                    GameProfileModel game = new GameProfileModel(
                            resultSet.getString("file_uuid"),
                            resultSet.getString("title"),
                            resultSet.getString("img_uuid"),
                            resultSet.getString("short_desc"),
                            resultSet.getString("genre_type")
                    );

                    games.add(game);
                }

                return games;
            }  catch (SQLException exception) {
                throw new RuntimeException("failed to get list of games for user " + uuid);
            }
    }
}
