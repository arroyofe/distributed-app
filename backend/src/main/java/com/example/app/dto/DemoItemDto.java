package com.example.app.dto;

import java.time.LocalDateTime;

/**
 * Dto correspondiente a los elementos DemoItemDto que no se han usado posteriormente
 * en la web desarrollado ulteriormente.
 *
 * @param id identificación numérica del elemento generada automáticamente.
 * @param name nombre del elemento.
 * @param description descripción del elemento.
 * @param createdAt Fecha de creación generada automáticamente.
 * @param updatedAt Fecha de actualización generada automáticament.
 */
public record DemoItemDto(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}