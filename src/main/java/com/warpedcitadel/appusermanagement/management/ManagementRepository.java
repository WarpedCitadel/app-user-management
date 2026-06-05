package com.warpedcitadel.appusermanagement.management;

import com.warpedcitadel.appusermanagement.management.model.SearchAttributesModel;
import com.warpedcitadel.appusermanagement.management.model.UserDetailsModel;
import com.warpedcitadel.appusermanagement.util.SQLFileReader;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DataSource database;

    SQLFileReader loadSQL = new SQLFileReader();


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


    public Slice<UserDetailsModel> getAppUsers(Pageable pageable, SearchAttributesModel attributes) {

        String selectSQL = loadSQL.loadSQL("/users/select--get_app_users.sql");
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();

        if (limit >= 51) {
            throw new IllegalArgumentException("Content requested too large");
        }

        List<Object> attributesList = new ArrayList<>();

        // Can this if/else block be reduced?
        if (attributes.getDisplayName() != null &&
                !attributes.getDisplayName().isEmpty()) {
            attributesList.add(attributes.getDisplayName().concat("%"));
        } else {
            attributes.setDisplayName("%");
            attributesList.add(attributes.getDisplayName());
        }
        if (attributes.getRole() != null &&
                !attributes.getRole().isEmpty()) {
            attributesList.add(attributes.getRole());
        } else {
            attributes.setRole(null);
            attributesList.add(attributes.getRole());
        }
        if (attributes.getIsActive() != null) {
            attributesList.add(attributes.getIsActive());
        } else {
            attributes.setIsActive(null);
            attributesList.add(attributes.getIsActive());
        }

        attributesList.add(limit + 1);
        attributesList.add(offset);

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

                UserDetailsModel user = new UserDetailsModel(
                        resultSet.getString("user_uuid"),
                        resultSet.getString("img_uuid"),
                        resultSet.getString("display_name"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("role_type"),
                        resultSet.getBoolean("isactive"),
                        resultSet.getString("created_dtm")
                );

                users.add(user);
            }

            boolean hasNext = users.size() > limit;

            if (hasNext) {
                users.remove(users.size() - 1);
            }

            return new SliceImpl<>(users, pageable, hasNext);

        } catch (SQLException exception){
            throw new RuntimeException("Failed to retrieve list of users", exception);
        }
    }
}
