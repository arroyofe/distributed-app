package com.example.web.services;

import com.example.web.dto.PokemonDto;
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

@Service
public class PokemonService {
    private final RestTemplate rest = new RestTemplate();

    //@Value("${services.py1.url}/pokemons")
    @Value("${services.py1.url:http://py1:5000}")
    private String py1Url; // utilizado en la creación de pokemones (usa hhtp.Entity)

    // Para el resto de métodos se usa la URL de base (http://py1:5000/pokemons)
    private String getBaseUrl() {
        return py1Url + "/pokemons";
    }

    public List<PokemonDto> findAll() {
        try {
            PokemonDto[] response = rest.getForObject(getBaseUrl(), PokemonDto[].class);
            return response != null ? Arrays.asList(response) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error de llamada a py1 : " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public PokemonDto getRandomPokemon() {
        return rest.getForObject(getBaseUrl() + "/random", PokemonDto.class);
    }

    public List<String> getAbilities(String name) {
        return rest.getForObject(getBaseUrl() + "/" + name + "/abilities", List.class);
    }


    public void create(PokemonDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PokemonDto> request = new HttpEntity<>(dto, headers);

        String urlFinale = py1Url.endsWith("/pokemons") ? py1Url : py1Url + "/pokemons";


        rest.exchange(urlFinale, HttpMethod.POST, request, String.class);

    }

    public void update(Long id, PokemonDto dto) {
        String url = "http://py1:5000/pokemons/" + id;
        System.out.println(">>> TENTATIVE UPDATE SUR : " + url);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PokemonDto> request = new HttpEntity<>(dto, headers);

            rest.exchange(url, HttpMethod.PUT, request, String.class);
        } catch (Exception e) {
            // En el log se ve el error si se intenta registrar sin cambios pero no bloquea al usuario
            System.err.println("Error al modificar(no bloqueante) : " + e.getMessage());
        }
    }


    public void delete(Long id) {
        // Correction ici aussi
        String url = py1Url + "/pokemons/" + id;
        System.out.println("DEBUG DELETE URL: " + url);
        rest.delete(url);
    }


    public PokemonDto findById(Long id) {

        return rest.getForObject(py1Url + "/" + id, PokemonDto.class);
    }
}


