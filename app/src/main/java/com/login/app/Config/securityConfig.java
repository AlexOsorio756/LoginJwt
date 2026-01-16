package com.login.app.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class securityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/api/users/**").permitAll() // Permitir registro
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/auth/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/index.html", true) // Aquí es donde irá tras el éxito
                .permitAll()
            )
            .logout(logout -> logout.permitAll());

        return http.build();
    }
}

