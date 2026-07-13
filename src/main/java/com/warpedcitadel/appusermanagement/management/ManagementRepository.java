package com.warpedcitadel.appusermanagement.management;

import com.warpedcitadel.appusermanagement.management.model.UserDetailsModel;
import com.warpedcitadel.appusermanagement.util.CloudFrontService;
import com.warpedcitadel.appusermanagement.util.SQLFileReader;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ManagementRepository {

    SQLFileReader loadSQL = new SQLFileReader();
    private final DataSource database;
    private final CloudFrontService cloudFrontService;

    public ManagementRepository(DataSource database, CloudFrontService cloudFrontService) {
        this.database = database;
        this.cloudFrontService = cloudFrontService;
    }


    public void disableAppUser(String uuid){

        String updateSQL = loadSQL.loadSQL("/users/update--disable_app_user.sql");

        try (Connection connection = database.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {

            updateStatement.setString(1, uuid);
            updateStatement.execute();

        } catch (SQLException exception) {
            throw new RuntimeException("Failed to disable user with the UUID: " + uuid, exception);
        }
    }


    public void enableAppUser(String uuid){

        String updateSQL = loadSQL.loadSQL("/users/update--enable_app_user.sql");

        try (Connection connection = database.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {

            updateStatement.setString(1, uuid);
            updateStatement.execute();

        } catch (SQLException exception) {
            throw new RuntimeException("Failed to enable user with the UUID: " + uuid, exception);
        }
    }


    public Slice<UserDetailsModel> getAppUsers(Pageable pageable, List<Object> attributesList) {

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_users.sql");

        List<UserDetailsModel> users = new ArrayList<>();

        try (Connection connection = database.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {

            int request;
            for (request = 0; attributesList.size() > request; request++) {

                if (attributesList.get(request) != null && !attributesList.isEmpty()) {
                    selectStatement.setObject(request + 1,
                            attributesList.get(request));
                } else {
                    selectStatement.setObject(request + 1, null);
                }
            }

            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {

                String profilePrefix = "images/users/" + resultSet.getString("img_uuid") +
                                        "/image/" + resultSet.getString("file_name");
                String profileImgUrl = cloudFrontService.generateSignedUrl(profilePrefix);

                UserDetailsModel user = new UserDetailsModel(
                        resultSet.getString("user_uuid"),
                        profileImgUrl,
                        resultSet.getString("display_name"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("role_type"),
                        resultSet.getBoolean("isactive"),
                        resultSet.getString("created_dtm")
                );

                users.add(user);
            }

            boolean hasNext = users.size() > pageable.getPageSize();

            if (hasNext) {
                users.remove(users.size() - 1);
            }

            return new SliceImpl<>(users, pageable, hasNext);

        } catch (SQLException exception){
            throw new RuntimeException("Failed to retrieve list of users", exception);
        }
    }
}
