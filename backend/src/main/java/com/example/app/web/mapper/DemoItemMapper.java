package com.example.app.web.mapper;

import com.example.app.domain.DemoItem;
import com.example.app.dto.DemoItemDto;
import com.example.app.dto.DemoItemCreateUpdateDto;

/**
 * Utilitario de conversián entre las entidades {@link DemoItem} del dominio y los
 * otros DTO utilizados por la capa web.
 *
 * Esta clase provee los métodos estáticos que permiten la transformacián de un
 * objet en DTO ou aplicar los datos de un DTO de creación/actualización de una
 * entidad existente. Centraliza la lógica del mapping para evitar la duplicación
 * del código en los controladores o servicios y garantiza una separación clara
 * entre los modelos internos y los objetos expuestos a través de la API.
 *
 * No tiene mecanismos lágicos, solamente se limit a la transformación de datos.
 */
public class DemoItemMapper {
    /**
     * Convierte una entidad {@link DemoItem} en {@link DemoItemDto} destinada a
     * la exposición a través de la API.
     *
     * @param e entidad que se ha de convertir.
     * @return un DTO que continen las informaciones públicas de la entidad.
     */
    public static DemoItemDto toDto(DemoItem e) {
        return new DemoItemDto(e.getId(), e.getName(), e.getDescription(), e.getCreatedAt(), e.getUpdatedAt());
    }

    /**
     * Aplica los datos de un DTO de creación/actualización a una entidad existente.
     * Ceste método se usa en las operaciones de actualización para sincronizarnel
     * campo modificable.
     * *
     * @param e   entidad para modificat.
     * @param dto datos provistos por el cliente.
     */
    public static void apply(DemoItem e, DemoItemCreateUpdateDto dto) {
        e.setName(dto.name());
        e.setDescription(dto.description());
    }
}