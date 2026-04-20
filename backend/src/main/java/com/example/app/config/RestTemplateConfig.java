package com.example.app.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Configuración centralizada del cliente HTTP utilisé por la aplicación para communicar con los
 * los servicios externos.
 *
 * Esta clase declara y configura un {@link org.springframework.web.client.RestTemplate}
 * compartido, y definen en particular Los timeouts de conexión y de lectura,
 *
 * Con ella se consigue asegurar un comportamiento coherente de las llamadas HTTP que
 * salend del backend, evitando la duplicación de la configuración de los diferentes servicios.
 */
@Configuration
public class RestTemplateConfig {
    /**
     * Constructor de la clase que configura los timeouts
     * @param builder inicial que se va a configurar
     * @return el builder con los parámetro escogidos
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }
}