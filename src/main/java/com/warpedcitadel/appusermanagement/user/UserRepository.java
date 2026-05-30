package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.security.AuthenticationModel;
import com.warpedcitadel.appusermanagement.user.profile.AppUserProfileModel;
import com.warpedcitadel.appusermanagement.user.profile.GameProfileModel;
import com.warpedcitadel.appusermanagement.user.usermanagement.SearchAttributesModel;
import com.warpedcitadel.appusermanagement.user.usermanagement.UserAuditModel;
import com.warpedcitadel.appusermanagement.user.usermanagement.UserDetailsModel;
import com.warpedcitadel.appusermanagement.util.SQLFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

        try (Connection connection = wcDatabase.getConnection();
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


    public AppUserProfileModel getAppUserProfile(String uuid) {

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_user_profile.sql");

        List<GameProfileModel> gameList = getUserGames(uuid);

        try (Connection connection = wcDatabase.getConnection();
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


    public int createAppUserProfile(AppUserProfileModel updateProfile){

        String updateSql = loadSQL.loadSQL("/users/insert--create_app_user_profile.sql");

        try (Connection connection = wcDatabase.getConnection();
            PreparedStatement updateStatement = connection.prepareStatement(updateSql, Statement.RETURN_GENERATED_KEYS)) {

            updateStatement.setInt(1, updateProfile.getAppUserId());
            updateStatement.setString(2, updateProfile.getDisplayName());
            updateStatement.setString(3, updateProfile.getBio());

            int rowAffected = updateStatement.executeUpdate();

            if (rowAffected == 1){
                try (ResultSet resultSet = updateStatement.getGeneratedKeys()) {
                    if (resultSet.next()) return resultSet.getInt(1);
                }
            }

            return -1;

        } catch (SQLException exception){
            throw new RuntimeException("Failed to create user profile with uuid: " + updateProfile.getUuid(), exception);
        }
    }


    public int updateAppUserProfile(AppUserProfileModel updateProfile){

        String updateSql = loadSQL.loadSQL("/users/update--update_app_user_profile.sql");

        try (Connection connection = wcDatabase.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateSql, Statement.RETURN_GENERATED_KEYS)) {

            updateStatement.setString(1, updateProfile.getDisplayName());
            updateStatement.setString(2, updateProfile.getBio());
            updateStatement.setInt(3, updateProfile.getAppUserId());

            int rowAffected = updateStatement.executeUpdate();

            if (rowAffected == 1){
                try (ResultSet resultSet = updateStatement.getGeneratedKeys()) {
                    if (resultSet.next()) return resultSet.getInt(1);
                }
            }

            return -1;

        } catch (SQLException updateException){
            throw new RuntimeException("Failed to update user profile with uuid: " +
                    updateProfile.getUuid(), updateException);
        }
    }


    public Slice<UserDetailsModel> getAppUsers(Pageable pageable, SearchAttributesModel attributes) {

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_users.sql");
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();

        if (limit >= 51) {
            throw new IllegalArgumentException("Content requested too large");
        }

        List<UserDetailsModel> users = new ArrayList<>();

        try (Connection connection = wcDatabase.getConnection();
        PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {

            selectStatement.setString(1, attributes.getDisplayName());
            selectStatement.setString(2, attributes.getRole());
            selectStatement.setBoolean(3, attributes.getIsActive());
            selectStatement.setInt(4, limit + 1);
            selectStatement.setInt(5, offset);

            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                UserDetailsModel user = new UserDetailsModel(
                        resultSet.getString("user_uuid"),
                        resultSet.getString("img_uuid"),
                        resultSet.getString("username"),
                        resultSet.getString("display_name"),
                        resultSet.getString("email"),
                        resultSet.getString("role_type"),
                        resultSet.getBoolean("isactive"),
                        resultSet.getString("created_dtm")
                );

                users.add(user);
            }

            boolean hasNext = users.size() > limit;

            if (hasNext){
                users.remove(users.size() - 1);
            }

            return new SliceImpl<>(users, pageable, hasNext);

        } catch (SQLException exception){
            exception.printStackTrace();
//            throw new RuntimeException("Failed to retrieve list of users", exception);
        }
        throw new RuntimeException("Failed to retrieve list of users");
    }


    public List<String> getAppUserSessions(String uuid) {

        String selectSQL = loadSQL.loadSQL("/audit/select--get_app_user_sessions.sql");

        List<String> userSessions = new ArrayList<>();

        try (Connection connection = wcDatabase.getConnection();
        PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {

            selectStatement.setString(1, uuid);

            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {

                String isSession = resultSet.getString(1);
                if (isSession == null) {
                    continue;
                }

                UserAuditModel session = new UserAuditModel(
                        resultSet.getString("lastactive_dtm")
                );

                userSessions.add(session.lastActiveDtm());
            }

            return userSessions;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to retrieve user session history", exception);
        }
    }


    // ################################### Helper Functions #########################################

    // Todo | Make create a statement to distinguish between a username and uuid

    public int getUserIdByUuid(String uuid){

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_user_id.sql");

        try (Connection connection = wcDatabase.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {

            selectStatement.setString(1, uuid);

            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException exception) {
            throw new RuntimeException("User with uuid: " + uuid + " does not exist", exception);
        }
        return -1;
    }


    public int getUserIdByUsername(String username) {

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_user_id_u.sql");

        try (Connection connection = wcDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException exception) {
            throw new UsernameNotFoundException("User with the username of " + username + " does not exist");
        }
        return -1;
    }

    public List<GameProfileModel> getUserGames(String uuid) {

            String selectSQL = loadSQL.loadSQL("/users/select--get_game_profiles.sql");

            List<GameProfileModel> games = new ArrayList<>();

            try (Connection connection = wcDatabase.getConnection();
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


    public void updateLastActiveDtm(String uuid){

        String updateSQL = loadSQL.loadSQL("/audit/update--update_last_active_dtm.sql");

        try (Connection connection = wcDatabase.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {

            updateStatement.setString(1, uuid);
            updateStatement.execute();

        } catch (SQLException exception) {
            throw new RuntimeException("failed to log user session");
        }
    }
}
