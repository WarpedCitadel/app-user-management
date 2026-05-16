package com.warpedcitadel.appusermanagement.security;


import com.warpedcitadel.appusermanagement.exceptionhandlers.exceptions.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    AuthTokenFilter authTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                        .csrf(csrf -> csrf.disable())
                        .sessionManagement(session -> session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        ))
                        .exceptionHandling(exception -> exception
                                .accessDeniedHandler(new CustomAccessDeniedHandler()))
                        .authorizeHttpRequests(auth ->
                                auth.requestMatchers("/api/v1/auth/**").permitAll()
                                        .requestMatchers("/error").permitAll()
                                        .requestMatchers("/api/v1/user/profile").hasAnyRole("admin", "mod", "user")
                                        .anyRequest().authenticated()
                        );
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
