package com.example.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Herramienta auxiliar para la generación rápida de hashes BCrypt.
 * <p>
 * Proporciona una utilidad simple para transformar cadenas de texto plano
 * en contraseñas encriptadas, facilitando la creación de registros de prueba
 * o credenciales iniciales en los scripts de migración de base de datos.
 */
@Slf4j
public class PasswordGenerator {

    /**
     * Genera y registra en el log el hash BCrypt de la cadena proporcionada.
     * <p>
     * Este método permite obtener rápidamente la versión cifrada de una contraseña
     * para asegurar que los datos sensibles no se almacenen en texto plano
     * durante la inicialización del sistema.
     */
    static void main() {
        log.info(
                new BCryptPasswordEncoder().encode(""));
    }
}
