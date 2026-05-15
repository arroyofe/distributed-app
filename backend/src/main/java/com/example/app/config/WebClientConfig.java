
package com.example.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuración del cliente Web utilizado por la aplicación para comunicar con el módulo py2
 * módulo Python externo al backend de java.
 * <p>
 * Esta clase expone un {@link WebClient} preconfigurado con la URL de base del servicio Python
 * lo cual permite efectuar llamadas HTTP salientes de manera centralizada y coherente.
 * <p>
 * El bean {@code py2WebClient} garantiza una configuración centralizada única para todos los
 * intercambios (usando la URL de base usando la propiedad {@code app.py2BaseUrl} así como una
 * configuración única y reutilizable de los intercambios con el múdulo y finalmente la posibilidad
 * de añadir, a posteriori, interceptadores, timeouts u otras opciones de serialización.
 * <p>
 * Esta configuración evita la duplicación del código asegurando una comunicación homogénea entre el
 * backend Java et el módulo Python.
 */
@Configuration
public class WebClientConfig {


    /**
     * Crea y expone un {@link WebClient} preconfigurado para comunicarse con el
     * servicio Python externo (módulo py2).
     *
     * <p>El cliente utiliza como URL base el valor definido en la propiedad
     * {@code app.py2BaseUrl}, lo que permite centralizar la configuración de
     * todas las llamadas HTTP salientes hacia dicho servicio.</p>
     *
     * <p>Este bean facilita la reutilización del cliente, evita duplicación de
     * código y permite añadir posteriormente interceptores, timeouts u otras
     * configuraciones avanzadas de forma unificada.</p>
     *
     * @param baseUrl URL base del servicio Python, inyectada desde la configuración
     *                de la aplicación.
     * @return instancia configurada de {@link WebClient}.
     */
    @Bean
    public WebClient py2WebClient(@Value("${app.py2BaseUrl}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

}
