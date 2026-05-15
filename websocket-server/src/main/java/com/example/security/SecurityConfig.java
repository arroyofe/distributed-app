package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración principal de Spring Security para la aplicación.
 *
 * <p>Define las reglas de acceso a los endpoints REST y WebSocket, deshabilita
 * características no necesarias para este proyecto (como CSRF y logout) y
 * permite el acceso público a las rutas utilizadas por el sistema de chat.</p>
 *
 * <p>Esta configuración está optimizada para aplicaciones que combinan
 * WebSockets, STOMP y endpoints REST sin autenticación basada en sesiones.</p>
 */
@Configuration
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     *
     * <p>Incluye:</p>
     * <ul>
     *   <li>Deshabilitar CSRF para permitir WebSockets y peticiones REST simples.</li>
     *   <li>Permitir acceso público a rutas de registro, login y recursos estáticos.</li>
     *   <li>Permitir acceso a rutas WebSocket y STOMP necesarias para SockJS.</li>
     *   <li>Deshabilitar autenticación básica y logout.</li>
     * </ul>
     *
     * @param http objeto de configuración de seguridad HTTP.
     * @return la cadena de filtros configurada.
     * @throws Exception si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ws-register", "/ws-login",
                                "/chat/register", "/chat/chatlogin", "/chat",
                                "/css/**", "/js/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/app/**").permitAll()
                        .anyRequest().permitAll()
                )
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout.disable());

        return http.build();
    }
}

