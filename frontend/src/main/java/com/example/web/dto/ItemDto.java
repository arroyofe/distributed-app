package com.example.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotNull(message = "La descripción es obligatoria")
    private String description;

    // Getters & setters generados automáticamente por lombook con las anotaciones en la clase
    // No Args y All Args permiten la instaciación del Dto par thymeleaf et Rest



}