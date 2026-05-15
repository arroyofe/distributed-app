package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de seguridad encargada de definir el codificador de contraseñas
 * utilizado en el sistema.
 *
 * <p>Expone un {@link PasswordEncoder} basado en {@link BCryptPasswordEncoder},
 * el cual se emplea para almacenar contraseñas de forma segura en la base de datos.</p>
 *
 * <p>Esta clase se registra como configuración de Spring mediante {@code @Configuration}.</p>
 */
@Configuration
public class PasswordConfig {

    /**
     * Proporciona un codificador de contraseñas basado en BCrypt.
     *
     * <p>BCrypt es un algoritmo seguro y recomendado para hashing de contraseñas,
     * ya que incorpora salt y es resistente a ataques de fuerza bruta.</p>
     *
     * @return instancia de {@link PasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

