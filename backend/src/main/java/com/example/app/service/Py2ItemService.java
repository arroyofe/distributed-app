package com.example.app.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

/**
 * Servicio responsable de la comunicación con el módulo Python externo.
 *
 * Esta clase encapsula el uso de {@link WebClient} que está dedicado a los intercambios
 * con el servicio Python, proporcionando una capa de abstracción clara entre
 * el backend Java y el componente externo. Centraliza las llamadas HTTP salientes y
 * proporciona los métodos simples para recuperar, crear o superior des elementos
 * gestionados por el módelo Python.
 *
 * El uso de un cliente Web permite evitar la duplicación de configuración HTTP, asegurar una
 * comunicación coherente, facilita los tests y separa claramente la lógica oficio de la
 * lógica de llamada externa.
 *
 * Los métodos de este servicio usan {@code block()} para simplificar la llamada
 * en un contexto no reactivo y adaptado  si el resto de la aplicacián funciona de
 * manera síncrona.
 */
@Service
public class Py2ItemService {

    private final WebClient web;

    /**
     * Constructor de la clase
     * @param py2WebClient enlace con Python
     */
    public Py2ItemService(WebClient py2WebClient) {
        this.web = py2WebClient;
    }

    /**
     * Recupera la lista completa de elementos expuestoss por el módulo Python.
     *
     * @return una lista de objetos JSON representados en form de {@code Map<String, Object>}
     */
    public List<Map<String, Object>> getItems() {
        return web.get()
                .uri("/items")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .collectList()
                .block();
    }

    /**
     * Envoía una petición de creación de un nuevo elemento al módulo Python.
     *
     * @param payload datos JSON que representan el elemento a crear
     * @return la respuesta devuelta por el módulo Python, en forma de map JSON
     */
    public Map<String, Object> addItem(Map<String, Object> payload) {
        return web.post()
                .uri("/items")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    /**
     * Solicita la supresión de un elemento identificado por su id.
     *
     * @param id identificación del elemento que se quiere suprimir.
     */
    public void deleteItem(long id) {
        web.delete()
                .uri("/items/{id}", id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}