package com.warpedcitadel.appusermanagement.auth;

import com.warpedcitadel.appusermanagement.audit.AuditRepository;
import com.warpedcitadel.appusermanagement.auth.dto.UserLoginDto;
import com.warpedcitadel.appusermanagement.auth.dto.UserReferenceDto;
import com.warpedcitadel.appusermanagement.auth.dto.UserSignupDto;
import com.warpedcitadel.appusermanagement.auth.dto.VerificationTokenDto;
import com.warpedcitadel.appusermanagement.auth.model.AuthModel;
import com.warpedcitadel.appusermanagement.auth.model.EmailVerificationModel;
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

import java.security.SecureRandom;
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

    private final String mailToken;

   private AuthService(@Value("${mail.trap.token}") String mailToken) {
        this.mailToken = mailToken;
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

       if (!dbUser.isActive() || !dbUser.isVerified()){
           throw new BadCredentialsException("Unactivated or disabled user");
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


   public String createAppUser(UserSignupDto userDto) {

       String encodedPassword = passwordEncoder().encode(userDto.password());
       String passcode = generateOTP(6);
       String token = createToken();

       UserModel userModel = new UserModel(
               userDto.username(),
               encodedPassword,
               userDto.email(),
               token,
               passcode
       );

       authRepository.createAppUser(userModel);
       sendActivationEmail(userModel, passcode);
       return token;
   }


   public void accountVerification(VerificationTokenDto emailToken) throws RuntimeException {

       EmailVerificationModel verificationToken = authRepository.emailVerificationToken(emailToken);

          if (verificationToken.isUsed()) {
              throw new RuntimeException("Token has been already used");
          }
          if (!verificationToken.getToken().equals(emailToken.token())) {
              throw new RuntimeException("Token is invalid");
          }
          if (!verificationToken.getPasscode().equals(emailToken.passcode())) {
              throw new RuntimeException("Passcode is invalid");
          }
           authRepository.verifyEnableUser(verificationToken.getAppUserId());
           authRepository.updateTokenStatus(verificationToken.getToken());
   }


   protected void sendActivationEmail(UserModel userModel, String passcode) {

       String messageBlock = """
              Hi %s,
              
              Welcome to Warped Citadel!
              
              We’re excited to have you join the Citadel. To complete your registration and unlock full access
              to your account, please enter the verification code shown below to the application:
              
              Your verification code is: %s
              
              
              Note: This passcode will expire in 15 minutes. If you need a new passcode, simply request a new verification passcode
              from the login page on our website.
              
              
              Didn't create an account? If you received this email by mistake, someone likely
              entered your email address in error. You can safely ignore this email; no account will be created without
              your explicit verification. If you have any questions or need a hand getting started, we're always here to help!
              Just reply directly to this email.
              
              Best regards,
              
              The Warped Citadel Support Team
              """.formatted(userModel.getUsername(), passcode);

       try {

           final MailtrapConfig config = new MailtrapConfig.Builder()
                   .token(mailToken)
                   .build();
           final MailtrapClient client = MailtrapClientFactory.createMailtrapClient(config);

           final MailtrapMail mail = MailtrapMail.builder()
                   .from(new Address("support@warpedcitadel.com", "Warped Citadel Support"))
                   .to(List.of(new Address(userModel.getEmail())))
                   .subject("Verify your email to get started with Warped Citadel")
                   .text(messageBlock)
                   .category("Account Verification")
                   .build();

           client.send(mail);
       } catch (Exception exception) {
            System.out.println("Caught exception : " + exception);
       }
   }


   // ##### HELPER FUNCTIONS #####

   private String createToken() {

       return UUID.randomUUID().toString();
   }


   private static String generateOTP(int length) {

       String numbers = "123456789";

       SecureRandom random = new SecureRandom();
       StringBuilder passcode = new StringBuilder(length);

       for (int i = 0; i < length; i++) {

           int index = random.nextInt(numbers.length());
           passcode.append(numbers.charAt(index));
       }

       return passcode.toString();
   }
}
