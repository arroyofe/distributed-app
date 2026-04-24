package com.example.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Objeto de transferencia de datos para la gestión centralizada de errores.
 * <p>
 * Se utiliza para estructurar la información de excepciones y fallos del sistema
 * antes de ser enviada a la vista o a un cliente externo.
 */
@Getter
@Setter
public class ErrorInfoDto {
    /** Tipo de excepción o error técnico. */
    private String type;

    /** Descripción amigable del error para el usuario. */
    private String message;

    /** Clasificación del error (ej. Seguridad, Base de Datos, Validación). */
    private String category;

    /** Detalle técnico de la pila de llamadas (uso principal en entorno de desarrollo). */
    private String stacktrace;
}
