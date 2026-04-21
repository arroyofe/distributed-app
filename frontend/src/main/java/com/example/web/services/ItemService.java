package com.example.web.services;

import com.example.web.dto.ItemDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Servicio de lógica de negocio para la gestión de ítems.
 * <p>
 * Actúa como un cliente REST que consume el microservicio Python (py2).
 * La comunicación se realiza de forma interna para gestionar el catálogo de objetos
 * compartidos en el ecosistema del proyecto.
 */
@Service
public class ItemService {

    private final RestTemplate rest = new RestTemplate();

    /** URL base del microservicio Python (py2), inyectada desde la configuración. */
    @Value("${services.py2.items-url}")
    private String py2Url;

    /**
     * Obtiene la lista completa de ítems desde el servicio Python.
     *
     * @return Lista de {@link ItemDto} con todos los objetos del catálogo.
     */
    public List<ItemDto> findAll() {
        ItemDto[] items = rest.getForObject(py2Url, ItemDto[].class);
        return Arrays.asList(items);
    }

    /**
     * Recupera un ítem específico por su identificador.
     *
     * @param id Identificador único del ítem.
     * @return El {@link ItemDto} correspondiente o null si no se encuentra.
     */
    public ItemDto findById(Long id) {
        return rest.getForObject(py2Url + "/" + id, ItemDto.class);
    }

    /**
     * Envía la orden de creación de un nuevo ítem al servicio Python.
     * <p>
     * Incluye una lógica de saneamiento básica: si la descripción está vacía,
     * se asigna el valor por defecto "N/A".
     *
     * @param item Objeto DTO con la información del nuevo ítem.
     */
    public void create(ItemDto item) {
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            item.setDescription("N/A");
        }
        rest.postForObject(py2Url, item, ItemDto.class);
    }

    /**
     * Actualiza un ítem existente mediante una petición PUT al servicio Python.
     *
     * @param id   Identificador del ítem a modificar.
     * @param item Objeto con los nuevos datos a persistir.
     */
    public void update(Long id, ItemDto item) {
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            item.setDescription("N/A");
        }
        rest.put(py2Url + "/" + id, item);
    }

    /**
     * Solicita la eliminación de un ítem al servicio Python.
     *
     * @param id Identificador del ítem a suprimir.
     */
    public void delete(Long id) {
        rest.delete(py2Url + "/" + id);
    }
}
