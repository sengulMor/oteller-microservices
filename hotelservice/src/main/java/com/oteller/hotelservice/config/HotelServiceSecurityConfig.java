package com.oteller.hotelservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class HotelServiceSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/hotel-service/**").authenticated()  // Protect the routes
                .anyRequest().permitAll()  // Allow any other requests
                .and()
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());  // To validate JWT token
        return http.build();
    }

}
