package com.example.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Herramienta auxiliar para la generación rápida de hashes BCrypt.
 * <p>
 * Esta clase se utiliza exclusivamente durante el desarrollo o la fase de despliegue
 * para convertir contraseñas en texto plano en valores cifrados compatibles con el
 * sistema de autenticación. No forma parte del flujo normal de la aplicación.
 * <p>
 * El método {@link #generate(String)} puede ejecutarse manualmente desde el IDE
 * para obtener un hash seguro que luego puede insertarse en la base de datos
 * (por ejemplo, mediante scripts Flyway).
 */
@Slf4j
public class PasswordGenerator {
    /**
     * Constructor privado para impedir la instanciación.
     * Esta clase es un utilitario puramente estático.
     */
    private PasswordGenerator() {
        // Dejado vacío a propósito para evitar la instanciaciób
    }
    /**
     * Genera y registra en el log el hash BCrypt de la cadena proporcionada.
     * Este método debe ejecutarse manualmente por un desarrollador cuando se
     * necesite obtener una contraseña cifrada para pruebas o inicialización.
     *
     * @param rawPassword contraseña en texto plano a cifrar
     */
    public static void generate(String rawPassword) {
        String hash = new BCryptPasswordEncoder().encode(rawPassword);
        log.info("Generated BCrypt hash = {}", hash);
    }
}
