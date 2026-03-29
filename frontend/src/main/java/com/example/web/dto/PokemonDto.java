package com.example.web.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PokemonDto {
    private Long id;
    private String name;
    private String type;
    private Integer level;
    // Getters et Setters y Data con Lombok)

}
