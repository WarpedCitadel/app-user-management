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

    private static final String TOKEN = "d327112a1a50060c7eeb61e90807f5be";

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
                .sandbox(true)
                .inboxId(4703606L)
                .token(TOKEN)
                .build();

        final MailtrapClient client = MailtrapClientFactory.createMailtrapClient(config);

        final MailtrapMail mail = MailtrapMail.builder()
                .from(new Address("hello@example.com", "Mailtrap Test"))
                .to(List.of(new Address("officalwarpedcitadel@gmail.com")))
                .subject("You are awesome!")
                .text("Congrats for sending test email with Mailtrap!")
                .category("Integration Test")
                .build();

        try {
            System.out.println(client.send(mail));
        } catch (Exception e) {
            System.out.println("Caught exception : " + e);
        }
    }
}
