package com.warpedcitadel.appusermanagement.authtest;

import com.warpedcitadel.appusermanagement.audit.AuditRepository;
import com.warpedcitadel.appusermanagement.auth.AuthRepository;
import com.warpedcitadel.appusermanagement.auth.AuthService;
import com.warpedcitadel.appusermanagement.auth.dto.UserLoginDto;
import com.warpedcitadel.appusermanagement.auth.dto.UserReferenceDto;
import com.warpedcitadel.appusermanagement.auth.dto.UserSignupDto;
import com.warpedcitadel.appusermanagement.auth.model.AuthModel;
import com.warpedcitadel.appusermanagement.auth.model.UserModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private AuthService authService;

    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {

        passwordEncoder = new BCryptPasswordEncoder(4);
    }

    @Test
    void _test_createAppUser() {

        UserSignupDto userDto = new UserSignupDto(
                "JohnBlanche",
                "pa$sW0rd5!",
                "JohnBlanche@gmail.com");

        authService.createAppUser(userDto);
        ArgumentCaptor<UserModel> user = ArgumentCaptor.forClass(UserModel.class);
        Mockito.verify(authRepository).createAppUser(user.capture());

        UserModel testUser = user.getValue();

        Assertions.assertNotNull(testUser);
        Assertions.assertEquals("JohnBlanche", testUser.getUsername());
        Assertions.assertEquals("johnblanche@gmail.com", testUser.getEmail());
        Assertions.assertTrue(passwordEncoder.matches("pa$sW0rd5!", testUser.getPasswordHash()));
    }


    @Test
    void _test_loginUser() {

        // Arrange
        UserLoginDto userDto = new UserLoginDto(
                "JohnBlanche",
                "pa$sW0rd5!");

        UserModel userModel = new UserModel(
                userDto.username()
        );

        AuthModel mockDbUser = new AuthModel(
                "019ea371-9498-7cb1-b4b9-4ee3db8dc132",
                "JohnBlanche",
                "$2a$10$8Hdtn/Ih2Pjd1V5780RVHe8NOLnZZFdjOyk1kax8CpDFHInsDG7A6",
                "user"
        );

        Mockito.when(authRepository.authenticateUser(userModel.getUsername())).thenReturn(mockDbUser);

        UserReferenceDto result = authService.loginUser(userDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(
                userDto.username(),
                mockDbUser.getUsername());
        Assertions.assertTrue(
                passwordEncoder.matches(userDto.password()
                        , mockDbUser.getPasswordHash()));
        Assertions.assertEquals(
                (result.userUUID()),
                mockDbUser.getUuid());

        Mockito.verify(auditRepository, Mockito.times(1)).updateLastActiveDtm(mockDbUser.getUuid());
        }
    }