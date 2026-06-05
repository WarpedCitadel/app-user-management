package com.warpedcitadel.appusermanagement.auth;

import com.warpedcitadel.appusermanagement.audit.AuditRepository;
import com.warpedcitadel.appusermanagement.user.model.UserModel;
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


    public AuthModel loginUser(UserModel user) {
        AuthModel dbUser = authRepository.authenticateUser(user.getUsername());
        String storedHash = dbUser.getPasswordHash();
        if (user.getUsername().equals(dbUser.getUsername())){
            if (BCrypt.checkpw(user.getPasswordHash(), storedHash)) {

                auditRepository.updateLastActiveDtm(dbUser.getUuid());
                return dbUser;
            }
        }
        throw new UsernameNotFoundException("Invalid user name or password");
    }


    public int registerUser(UserModel user) {
        String encodedPassword = passwordEncoder().encode(user.getPasswordHash());
        user.setPasswordHash(encodedPassword);

        return authRepository.registerUser(user);
    }
}
