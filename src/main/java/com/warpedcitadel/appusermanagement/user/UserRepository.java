package com.warpedcitadel.appusermanagement.user;

import com.warpedcitadel.appusermanagement.security.AuthenticationModel;
import com.warpedcitadel.appusermanagement.user.profile.AppUserProfileModel;
import com.warpedcitadel.appusermanagement.user.profile.GameProfileModel;
import com.warpedcitadel.appusermanagement.util.SQLFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
                        resultSet.getString("uuid"),
                        resultSet.getString("username"),
                        resultSet.getString("password_hash"),
                        resultSet.getString("role_type")
                );
                return dbUser;
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to connect to database", exception);
        }
        throw new RuntimeException("User with the username of " + username + " was not found");
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


    public AppUserProfileModel getAppUserProfile(String uuid) throws SQLException {

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_user_profile.sql");

        List<GameProfileModel> games = getUserGames(uuid);

        try (Connection connection = wcDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {

            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new AppUserProfileModel(
                        resultSet.getString("uuid"),
                        resultSet.getString("display_name"),
                        resultSet.getString("user_bio"),
                        games
                );
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find user with uuid: " + uuid);
        }
        throw new RuntimeException("Failed to connect connect to the Database");
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

        } catch (SQLException updateException){
            throw new RuntimeException("Failed to update user profile with uuid: " + updateProfile.getUuid(), updateException);
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
            throw new RuntimeException("Failed to update user profile with uuid: " + updateProfile.getUuid(), updateException);
        }
    }


    public Slice<AppUserProfileModel> findUsers(Pageable pageable, String displayName) {

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_users.sql");
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();

        List<AppUserProfileModel> users = new ArrayList<>();

        try (Connection connection = wcDatabase.getConnection();
        PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {

            selectStatement.setString(1, displayName);
            selectStatement.setInt(2, limit + 1);
            selectStatement.setInt(3, offset);

            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                AppUserProfileModel user = new AppUserProfileModel(
                        resultSet.getString("uuid"),
                        resultSet.getString("username")
                );

                users.add(user);
            }

            boolean hasNext = users.size() > limit;

            if (hasNext){
                users.remove(users.size() - 1);
            }

            return new SliceImpl<>(users, pageable, hasNext);

        } catch (SQLException exception){
            throw new RuntimeException("Can not load users", exception);
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


    public int getUserIdByUsername(String username) throws SQLException{

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_user_id_u.sql");

        try (Connection connection = wcDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectSQL)) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException exception) {
            throw new SQLException("User with username: " + username + " does not exist", exception);
        }
        return -1;
    }

    public List<GameProfileModel> getUserGames(String uuid) throws SQLException {

            String selectSQL = loadSQL.loadSQL("/users/select--get_game_profiles.sql");

            List<GameProfileModel> games = new ArrayList<>();

            try (Connection connection = wcDatabase.getConnection();
            PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {

                selectStatement.setString(1, uuid);

                ResultSet resultSet = selectStatement.executeQuery();

                while (resultSet.next()) {
                    GameProfileModel game = new GameProfileModel(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5)
                    );

                    games.add(game);
                }

                return games;
            }  catch (SQLException exception) {
                throw new SQLException("Can not get list of games for user id of " + uuid, exception);
            }
    }

}
