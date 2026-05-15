package com.example.app.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * Controlador REST que juega el rol de proxy entre el backend Java y el módulo Python.
 * <p>
 * Esta clase expone endpoints que redirigen las peticiones hacia el servicio Python,
 * utilizando principalmente un {@link WebClient} configurado para ese uso. En caso
 * de fracaso de la llamada WebClient (error de red, indisponibilidad, timeout), un mecanismo
 * de repliegue (fallback) basado en {@link RestTemplate} se usa para mejorar la resiliencia
 * de la intégración.
 * <p>
 * El controlador consigue de esta manera encapsuler la lógica de la llamada al módulo Python,
 * así como ofrecer una API estable incluso en caso de problemas del servicio Python,
 * lo que permite recibir respuestas de error coherentes en caso de fallo de las llamadas externas.
 * <p>
 * La URL de base del módulo Python se inyecta gracias a la propiedad {@code app.py2BaseUrl}.
 */
@RestController
@RequestMapping("/api/py2")
public class Py2ProxyController {

    private final WebClient py2WebClient;
    private final RestTemplate restTemplate;
    private final String py2BaseUrl;

    /**
     * Constructor que inicializa el proxy con los clientes HTTP necesarios y  la URL del servicio Python.
     *
     * @param py2WebClient cliente WebClient configurado para communicar con el módulo Python
     * @param restTemplate cliente RestTemplate utilizado como fallback
     * @param py2BaseUrl URL de base del servicio Python
     */
    public Py2ProxyController(
            WebClient py2WebClient,
            RestTemplate restTemplate,
            @Value("${app.py2BaseUrl}") String py2BaseUrl) {

        this.py2WebClient = py2WebClient;
        this.restTemplate = restTemplate;
        this.py2BaseUrl = py2BaseUrl;
    }

    /**
     * Proxy hacia el microservico Python.
     * <p>
     * Este método envia una peticion GET hacia el endpoint /predict del servicio Python
     * para ello usa WebClient. La respuesta JSON que devuelve Python se envia directamente
     * al cliente en forma de Map.
     * <p>
     * No hay logica aplicada en este método: este controlador sirve unicamente de pasarela
     * entre el backend Java y el modulo Python.
     *
     * @return la respuesta para la pasarela
     */
    @GetMapping("/predict")
    public Map<String, Object> proxyPredict() {
        try {
            return py2WebClient
                    .get()
                    .uri(py2BaseUrl + "/predict")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

        } catch (Exception ex) {
            // Fallback RestTemplate
            ResponseEntity<Map<String, Object>> response =
                    restTemplate.exchange(
                            py2BaseUrl + "/predict",
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<Map<String, Object>>() {}
                    );
            return response.getBody();
        }
    }


}