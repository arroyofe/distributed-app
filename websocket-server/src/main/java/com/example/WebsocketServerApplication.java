package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Clase principal de la aplicación WebSocket Server.
 *
 * <p>Inicializa el contexto de Spring Boot, habilita el escaneo de entidades
 * JPA y repositorios, y arranca el servidor que gestiona WebSockets,
 * mensajería STOMP y endpoints REST.</p>
 *
 * <p>Esta clase actúa como punto de entrada de la aplicación.</p>
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.repository")
@EntityScan(basePackages = "com.example.model")
public class WebsocketServerApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(WebsocketServerApplication.class, args);
    }
}

