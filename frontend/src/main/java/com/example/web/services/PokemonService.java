package com.example.web.services;

import com.example.web.dto.PokemonDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Servicio encargado de la comunicación con el microservicio de gestión Pokémon (py1).
 * <p>
 * Este servicio actúa como cliente REST para realizar operaciones CRUD y consultas
 * especializadas (aleatorios, habilidades) sobre la base de datos de Pokémon
 * gestionada por el módulo Python.
 */
@Slf4j
@Service
public class PokemonService {
    private static final String POKE = "/pokemons";
    private final RestTemplate rest = new RestTemplate();

    /** URL base del microservicio py1, obtenida de la configuración o valor por defecto. */
    @Value("${services.py1.url:http://py1:5000}")
    private String py1Url;

    /** @return La URL completa para el endpoint de Pokémon. */
    private String getBaseUrl() {
        return py1Url + POKE;
    }

    /**
     * Obtiene todos los Pokémon registrados.
     *
     * @return Lista de {@link PokemonDto} o una lista vacía en caso de error de conexión.
     */
    public List<PokemonDto> findAll() {
        try {
            PokemonDto[] response = rest.getForObject(getBaseUrl(), PokemonDto[].class);
            return response != null ? Arrays.asList(response) : Collections.emptyList();
        } catch (Exception e) {
            log.error("Error de llamada a py1 : {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /** @return Un Pokémon seleccionado de forma aleatoria por el microservicio. */
    public PokemonDto getRandomPokemon() {
        return rest.getForObject(getBaseUrl() + "/random", PokemonDto.class);
    }

    /**
     * Recupera las habilidades de un Pokémon específico por su nombre.
     *
     * @param name Nombre del Pokémon.
     * @return Lista de nombres de habilidades.
     */
    public List<String> getAbilities(String name) {
        return rest.getForObject(getBaseUrl() + "/" + name + "/abilities", List.class);
    }

    /**
     * Registra un nuevo Pokémon enviando un objeto JSON al microservicio.
     *
     * @param dto Datos del Pokémon a crear.
     */
    public void create(PokemonDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PokemonDto> request = new HttpEntity<>(dto, headers);

        String urlFinale = py1Url.endsWith(POKE) ? py1Url : py1Url + POKE;
        rest.exchange(urlFinale, HttpMethod.POST, request, String.class);
    }

    /**
     * Actualiza los datos de un Pokémon existente.
     * <p>
     * Las excepciones se capturan en el log para evitar interrupciones en la UI
     * si la actualización no detecta cambios en el servidor.
     *
     * @param id  ID del Pokémon.
     * @param dto Nuevos datos.
     */
    public void update(Long id, PokemonDto dto) {
        String url = "http://py1:5000/pokemons/" + id;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PokemonDto> request = new HttpEntity<>(dto, headers);
            rest.exchange(url, HttpMethod.PUT, request, String.class);
        } catch (Exception e) {
            log.error("Error al modificar (no bloqueante) : {}", e.getMessage());
        }
    }

    /** @param id ID del Pokémon a eliminar. */
    public void delete(Long id) {
        rest.delete(py1Url + "/pokemons/" + id);
    }

    /** @param id ID del Pokémon a buscar. @return El Pokémon encontrado. */
    public PokemonDto findById(Long id) {
        return rest.getForObject(py1Url + "/" + id, PokemonDto.class);
    }
}
