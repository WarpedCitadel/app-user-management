package com.warpedcitadel.appusermanagement.management;

import com.warpedcitadel.appusermanagement.util.SQLFileReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManagementRepositoryTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    private SQLFileReader loadSQL = new SQLFileReader();

    @InjectMocks
    private ManagementRepository managementRepository;

    @Test
    void disableAppUser() throws SQLException {

        String updateSQL = loadSQL.loadSQL("/users/update--disable_app_user.sql");
        String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(updateSQL)).thenReturn(preparedStatement);

        managementRepository.disableAppUser(validUUID);

        verify(preparedStatement).setString(1, validUUID);
        verify(preparedStatement).execute();
    }

    @Test
    void enableAppUser() throws SQLException {

        String updateSQL = loadSQL.loadSQL("/users/update--enable_app_user.sql");
        String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(updateSQL)).thenReturn(preparedStatement);

        managementRepository.enableAppUser(validUUID);

        verify(preparedStatement).setString(1, validUUID);
        verify(preparedStatement).execute();
    }

    @Test
    void getAppUsers() {
    }
}