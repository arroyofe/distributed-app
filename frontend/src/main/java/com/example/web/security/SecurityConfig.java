package com.example.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // .csrf(csrf -> csrf.disable())  Provisional sin encriptación


                .authorizeHttpRequests(auth -> auth
                        // Ressources statiques
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/css/**", "/images/**", "/style.css").permitAll()

                        // Pages publiques
                        .requestMatchers("/", "/home", "/index").permitAll()

                        // Items accessibles à tous les utilisateurs connectés
                        .requestMatchers("/items", "/items/**").hasAnyRole("ADMIN", "DEV", "USER")

                        // Login / Logout
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/logout").permitAll()

                        // Zones protégées
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/dev/**").hasAnyRole("DEV", "ADMIN")

                        // Tout le reste nécessite authentification
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/items", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        // System.out.println(">>> Using MY SecurityFilterChain <<<"); PROVISIONAL PARA TESTS
        return http.build();
    }

    /* PROVISIONAL para tests
    @Bean
    public ApplicationRunner debugChains(List<SecurityFilterChain> chains) {
        return args -> {
            System.out.println(">>> SecurityFilterChains found:");
            chains.forEach(c -> System.out.println(" - " + c));
        };
    }

    @PostConstruct
    public void debug() {
        System.out.println(">>> SecurityConfig ACTIVE <<<");
    }
    */
}