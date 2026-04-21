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

/**
 * Configuración central de seguridad de la aplicación.
 * <p>
 * Define las políticas de acceso, el filtrado de peticiones HTTP, la gestión de
 * sesiones y la encriptación de credenciales. Utiliza anotaciones de seguridad
 * a nivel de método para un control granular.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_DEV = "DEV";

    /**
     * Define la cadena de filtros de seguridad y las reglas de autorización por URL.
     * <p>
     * Configuración detallada:
     * <ul>
     *   <li>Rutas públicas: Acceso libre a página de inicio, recursos estáticos y errores.</li>
     *   <li>Gestión de Usuarios: Restringida exclusivamente al rol {@code ADMIN}.</li>
     *   <li>Espacio Desarrollador: Acceso permitido a roles {@code DEV} y {@code ADMIN}.</li>
     *   <li>Pokémon: Consulta permitida a usuarios autenticados; edición restringida a {@code ADMIN}.</li>
     * </ul>
     *
     * @param http Configuración de seguridad HTTP.
     * @return La cadena de filtros configurada.
     * @throws Exception Si ocurre un error en la definición de la configuración.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // Desactivado provisionalmente para facilitar el desarrollo/testeo

                .authorizeHttpRequests(auth -> auth
                        // ACCESO PÚBLICO: Páginas base y recursos estáticos (CSS, JS, imágenes)
                        .requestMatchers("/", "/home", "/index", "/login", "/error/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**", "/webjars/**", "/videos/**", "/docs/**").permitAll()

                        // ÍTEMS: Consulta pública, pero gestión protegida
                        .requestMatchers(HttpMethod.GET, "/items", "/items/*").permitAll()
                        .requestMatchers("/items/new", "/items/edit/**", "/items/delete/**").authenticated()

                        // ADMINISTRACIÓN: Gestión de usuarios
                        .requestMatchers("/users/**").hasRole(ROLE_ADMIN)

                        // DESARROLLO: Herramientas de diagnóstico
                        .requestMatchers("/dev-dashboard", "/dev/**").hasAnyRole(ROLE_DEV, ROLE_ADMIN)

                        // POKÉMON: Consulta protegida y gestión exclusiva para ADMIN
                        .requestMatchers(HttpMethod.GET, "/pokemons").authenticated()
                        .requestMatchers("/pokemons/admin/**", "/pokemons/new/**", "/pokemons/edit/**", "/pokemons/delete/**").hasRole(ROLE_ADMIN)

                        // SEGURIDAD POR DEFECTO: Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )

                // CONFIGURACIÓN DEL LOGIN: Página personalizada y ruta de éxito
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/index", false)
                        .permitAll()
                )

                // CONFIGURACIÓN DEL LOGOUT: Ruta de salida y página de confirmación
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/logout-success")
                        .permitAll()
                );

        return http.build();
    }

    /**
     * Registra el dialecto de seguridad de Thymeleaf.
     * <p>
     * Permite utilizar atributos como {@code sec:authorize} en las vistas HTML
     * para mostrar u ocultar elementos según los roles del usuario.
     *
     * @return El dialecto de seguridad para el motor de plantillas.
     */
    @Bean
    public SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }

    /**
     * Define el algoritmo de encriptación para las contraseñas.
     * <p>
     * Utiliza un delegado que permite soportar múltiples formatos (por defecto BCrypt),
     * garantizando que las credenciales no se almacenen en texto plano en la base de datos.
     *
     * @return El codificador de contraseñas configurado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
