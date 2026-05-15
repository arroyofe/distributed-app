package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Clase principal de la aplicación web.
 *
 * <p>Inicializa el contexto de Spring Boot y configura el escaneo de componentes
 * para los paquetes {@code com.example.app} y {@code com.example.web}, lo que
 * permite detectar controladores, servicios y demás beans definidos en esas
 * rutas.</p>
 *
 * <p>Esta clase actúa como punto de entrada de la aplicación y arranca el
 * servidor embebido.</p>
 */
@SpringBootApplication(scanBasePackages = {
        "com.example.app",
        "com.example.web"
})
public class AppApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}
