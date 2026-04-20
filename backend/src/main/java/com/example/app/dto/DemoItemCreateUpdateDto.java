package com.example.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Representa los datos necesarios a la creación o actualización de un elemento Demo. Recuerdar
 *  que no se ha utilzado en la versión final de la web creada pero sirve de ejemplo de implementación.
 *
 * Se ha definido como {@code record} para obtener los beneficios de una estructura inmutable, concisa
 * y adaptada a los DTO. Usar un record permite evitar el código estáadar de una clase Java, con su
 * constructor, getters, setters, equals/hashCode, toString, etc; asegurando al mismo tiempo la
 * inmutabilidad de los datos lo que contribuye a la legibilidad y el mantenimiento del código para
 * los objetos que simplemente vehiculan datos.
 *
 * Las restricciones de validación Bean Validation se aplican directamente a los componentes del record
 * para garantizar la integridad de los datos recibidos en las operaciones de creación y actualización.
 *
 * @param name nombre del elemento, obilgatorio y limitado a 100 caracteres
 * @param description descripción opcional del elemnto limitado a 255 caracteres
 */
public record DemoItemCreateUpdateDto(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 255) String description
) {}