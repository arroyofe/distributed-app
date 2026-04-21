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
     * Ejecuta el proceso de cifrado y muestra el hash resultante en los logs.
     * <p>
     * Se utiliza para transformar contraseñas en claro en formatos seguros
     * compatibles con el motor de autenticación de la aplicación.
     */
    static void main() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // Genera el hash para una contraseña vacía o específica
        String hash = encoder.encode("");
        log.info("Hash admin = {}", hash);
    }
}

