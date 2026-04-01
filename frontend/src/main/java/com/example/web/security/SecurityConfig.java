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

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_DEV = "DEV";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // provisional desactivada

                .authorizeHttpRequests(auth -> auth
                        // PUBLICO
                        .requestMatchers("/", "/home", "/index", "/login", "/error/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**", "/webjars/**", "/videos/**", "/docs/**", "/js/**"
                        ).permitAll()

                        // ITEMS PUBLICO
                        .requestMatchers(HttpMethod.GET, "/items", "/items/*").permitAll()

                        // ITEMS ADMIN
                        .requestMatchers("/items/new", "/items/edit/**", "/items/delete/**").authenticated()

                        // USERS ADMIN
                        .requestMatchers("/users/**").hasRole(ROLE_ADMIN)

                        // DEV + ADMIN
                        .requestMatchers("/dev-dashboard", "/dev/**").hasAnyRole(ROLE_DEV, ROLE_ADMIN)

                        // POKEMON LISTA (Consulta)
                        .requestMatchers(HttpMethod.GET, "/pokemons").authenticated()

                        // GESTION POKEMON (Todas las acciones previtas en /pokemons/admin/**)
                        .requestMatchers("/pokemons/admin/**").hasRole(ROLE_ADMIN)

                        // GESTION POKEMON - ACCIONES específicas (POST para add/edit/delete)
                        .requestMatchers("/pokemons/new/**").hasRole(ROLE_ADMIN)
                        .requestMatchers("/pokemons/edit/**").hasRole(ROLE_ADMIN)
                        .requestMatchers("/pokemons/delete/**").hasRole(ROLE_ADMIN)

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

    //Gestión del dialecto Spring
    @Bean
    public SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}