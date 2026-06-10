package com.warpedcitadel.appusermanagement.auth;

import com.warpedcitadel.appusermanagement.audit.AuditRepository;
import com.warpedcitadel.appusermanagement.auth.dto.UserReferenceDto;
import com.warpedcitadel.appusermanagement.auth.dto.UserLoginDto;
import com.warpedcitadel.appusermanagement.auth.dto.UserSignupDto;
import com.warpedcitadel.appusermanagement.auth.model.AuthModel;
import com.warpedcitadel.appusermanagement.auth.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private AuditRepository auditRepository;

    @Bean
    private PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }


    public UserReferenceDto loginUser(UserLoginDto userDto) {

        UserModel userModel = new UserModel(
                userDto.username()
        );

        AuthModel dbUser = authRepository.authenticateUser(userModel.getUsername());
        String storedHash = dbUser.getPasswordHash();
        if (userDto.username().equals(dbUser.getUsername())){
            if (BCrypt.checkpw(userDto.password(), storedHash)) {

                auditRepository.updateLastActiveDtm(dbUser.getUuid());
                UserReferenceDto user = new UserReferenceDto(dbUser.getUuid());
                return user;
            }
        }
        throw new UsernameNotFoundException("Invalid user name or password");
    }


    public void createAppUser(UserSignupDto userDto) {
        String encodedPassword = passwordEncoder().encode(userDto.password());

        UserModel userModel = new UserModel(
                userDto.username(),
                encodedPassword,
                userDto.email());
        authRepository.createAppUser(userModel);
    }
}
