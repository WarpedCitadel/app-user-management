package com.warpedcitadel.appusermanagement.auth;

import com.warpedcitadel.appusermanagement.audit.AuditRepository;
import com.warpedcitadel.appusermanagement.auth.dto.UserLoginDto;
import com.warpedcitadel.appusermanagement.auth.dto.UserReferenceDto;
import com.warpedcitadel.appusermanagement.auth.dto.UserSignupDto;
import com.warpedcitadel.appusermanagement.auth.model.AuthModel;
import com.warpedcitadel.appusermanagement.auth.model.UserModel;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private JavaMailSender mailSender;

    private final String token;

   private AuthService(@Value("${mail.trap.token}") String token) {
        this.token = token;
    }

    @Bean
    private PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }


    public UserReferenceDto loginUser(UserLoginDto userDto) {

        UserModel userModel = new UserModel(
                userDto.username()
        );

        AuthModel dbUser = authRepository.authenticateUser(userModel.getUsername());

        if (!dbUser.isActive()){
            throw new BadCredentialsException("User not activated");
        }

        String storedHash = dbUser.getPasswordHash();
        if (userDto.username().equals(dbUser.getUsername())){
            if (BCrypt.checkpw(userDto.password(), storedHash)) {

                auditRepository.updateLastActiveDtm(dbUser.getUuid());
                return new UserReferenceDto(dbUser.getUuid());
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


    private String createToken() {

        String tokenUUID = UUID.randomUUID().toString();
        String hashedToken = passwordEncoder().encode(tokenUUID);
        return hashedToken;
    }


    protected void sendActivationEmail(UserSignupDto userDto) {

//        String token = createToken();

        final MailtrapConfig config = new MailtrapConfig.Builder()
                .token(token)
                .build();

        System.out.println(token);

        final MailtrapClient client = MailtrapClientFactory.createMailtrapClient(config);

        final MailtrapMail mail = MailtrapMail.builder()
                .from(new Address("support@warpedcitadel.com", "Activate Account"))
                .to(List.of(new Address(userDto.email())))
                .subject("Activate your Warped Citadel Account")
                .text("Please click on the following link to activate your account.")
                .build();

        try {
            System.out.println(client.send(mail));
        } catch (Exception e) {
            System.out.println("Caught exception : " + e);
        }
    }
}
