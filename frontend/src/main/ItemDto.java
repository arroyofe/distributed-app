package com.example.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objeto de transferencia de datos para la entidad Item.
 * <p>
 * Se utiliza para capturar los datos de los formularios de creación y edición,
 * aplicando reglas de validación en la capa de presentación antes de su procesamiento.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    /** Identificador único del ítem (nulo para nuevas creaciones). */
    private Long id;

    /** Nombre descriptivo del ítem. No puede estar vacío. */
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    /** Detalle extenso del ítem. Requerido para la persistencia. */
    @NotNull(message = "La descripción es obligatoria")
    private String description;
}
