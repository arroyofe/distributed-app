package com.example.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // si tu veux garder CSRF désactivé

                .authorizeHttpRequests(auth -> auth
                        // PUBLICO
                        .requestMatchers("/", "/home", "/index", "/login", "/error/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**", "/webjars/**").permitAll()

                        // ITEMS PUBLICO
                        .requestMatchers(HttpMethod.GET, "/items", "/items/*").permitAll()

                        // ITEMS ADMIN
                        .requestMatchers("/items/new", "/items/edit/**", "/items/delete/**").authenticated()

                        // USERS ADMIN
                        .requestMatchers("/users/**").hasRole("ADMIN")

                        // DEV + ADMIN
                        .requestMatchers("/dev-dashboard", "/dev/**").hasAnyRole("DEV", "ADMIN")

                        // POKEMON ADMIN
                        .requestMatchers("/pokemons/**").hasRole("ADMIN") // Seuls les admins voient les pokémons
                        .requestMatchers(HttpMethod.POST, "/pokemons/edit/**").hasRole("ADMIN")

                        // EL RESTO DE CASOS→ autentificación
                        .anyRequest().authenticated()
                )

                // LOGIN
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/index", false)
                        .permitAll()
                )

                // LOGOUT
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/logout-success")
                        .permitAll()
                );

        return http.build();
    }

    //Gestion del dialecto Spring
    @Bean
    public SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}