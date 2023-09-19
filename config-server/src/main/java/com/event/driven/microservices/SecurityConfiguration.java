package com.event.driven.microservices;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
            .ignoringAntMatchers("/encrypt/**")
            .ignoringAntMatchers("/decrypt/**")
            .ignoringAntMatchers("/actutor/**");

//        http.authorizeRequests()
//            .antMatchers("/encrypt/**")
//            .authenticated()
//            .antMatchers("/decrypt/**")
//            .authenticated();
        return http.build();
    }
}
