package com.example.web.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Objeto de transferencia de datos para la entidad Pokémon.
 * <p>
 * Representa la estructura de datos compartida entre el frontend Java y los
 * módulos Python (py1 y py2). Se utiliza para el intercambio de información
 * a través de la base de datos MySQL común.
 */
@Getter
@Setter
@Data
public class PokemonDto {

    /** Identificador único en la base de datos compartida. */
    private Long id;

    /** Nombre del Pokémon. */
    private String name;

    /** Tipo o categoría (ej. Fuego, Agua, Eléctrico). */
    private String type;

    /** Nivel de experiencia del Pokémon. */
    private Integer level;
}

