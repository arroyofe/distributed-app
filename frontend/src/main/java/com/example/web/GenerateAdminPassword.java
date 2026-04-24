package com.example.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad puntual para la generación de hashes de seguridad.
 * <p>
 * Esta clase se utiliza durante la fase de desarrollo o despliegue para generar
 * contraseñas encriptadas mediante el algoritmo BCrypt, destinadas a ser
 * insertadas manualmente o vía Flyway en la base de datos MySQL.
 */
@Slf4j
public class GenerateAdminPassword {
    /**
     * Constructor privado para impedir la instanciación.
     * Esta clase es un utilitario puramente estático.
     */
    private GenerateAdminPassword() {
        // Dejado vacío a propósito para evitar la instanciaciób
    }

    /**
     * Genera y muestra en los logs un hash BCrypt para la contraseña del usuario
     * administrador. Este método debe ejecutarse manualmente durante el desarrollo
     * o despliegue, y no forma parte del flujo normal de la aplicación.
     */
    public static void generate() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("");
        log.info("Hash admin = {}", hash);
    }
}

