package com.example.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Clase principal y punto de entrada del módulo Frontend.
 * <p>
 * Configura y arranca la aplicación Spring Boot, asegurando el escaneo de todos
 * los componentes, servicios y controladores dentro del paquete raíz.
 */
@SpringBootApplication(scanBasePackages = {
        "com.example.web"
})
public class FrontendApplication {

    /**
     * Método de inicio que lanza la aplicación Spring Boot.
     *
     * @param args Argumentos de línea de comandos pasados al arranque.
     */
    public static void main(String[] args) {
        SpringApplication.run(FrontendApplication.class, args);
    }

    /**
     * Define el bean de {@link RestTemplate} para el contexto de la aplicación.
     * <p>
     * Se configura de forma centralizada aquí para ser inyectado en los servicios
     * (como {@code ItemService} y {@code PokemonService}) que requieren comunicación
     * HTTP con los microservicios Python.
     *
     * @return una instancia única de RestTemplate.
     */
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
