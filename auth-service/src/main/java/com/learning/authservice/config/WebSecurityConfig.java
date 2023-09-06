package com.learning.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. All requests should be authenticated
        http.authorizeHttpRequests(
                //auth -> auth.anyRequest().authenticated()
                auth -> auth.anyRequest().permitAll()
        );
        // 2. If a request is not authenticated, a web page is shown
        http.httpBasic(withDefaults());
        // 3. Enabling CSRF check will impact POST and PUT requests. Disable it
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

}
