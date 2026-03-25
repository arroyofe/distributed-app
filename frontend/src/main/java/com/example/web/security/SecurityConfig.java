package com.example.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())  //Provisional sin encriptación usado en los tests

            .authorizeHttpRequests(auth -> auth


                // Lectura unicamente → público: accesible a todos los usuarios; sin contraseña
                .requestMatchers(HttpMethod.GET, "/items", "/items/*").permitAll()

                // CRUD → Accesible al ADMIN únicamente (login y contraseña admin)
                .requestMatchers("/items/create", "/items/edit/**", "/items/delete/**").hasRole("ADMIN")

                // Páginas de acceso público
                .requestMatchers("/", "/home", "/index", "/login", "/error/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**", "/webjars/**").permitAll()

                // Zonas reservadas a admin
                .requestMatchers("/users/**").hasRole("ADMIN")

                // Zona reservada a los desarrolladores (dev) y al admin
                .requestMatchers("/dev-dashboard", "/dev/**").hasAnyRole("DEV", "ADMIN")

                // Cualquier otro caso → neceista autentificación con contraseña
                .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/index", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/logout-success")
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