package com.example.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class PasswordGenerator {
    /**
     * Crea una contraseña hash equivalente al string pasado por argumento
     *
     */
    static void main() {
        log.info(
                new BCryptPasswordEncoder().encode(""));
    }
}