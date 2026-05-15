package com.example.app.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.Map;

/**
 * Gestor global de excepciones para todo el backend.
 *
 * <p>Esta clase centraliza el tratamiento de errores no interceptados por los
 * controladores REST y garantiza que todas las excepciones se traduzcan en una
 * respuesta JSON uniforme. Esto facilita el debugging, la interpretación de
 * errores y la integración con clientes externos (frontend, servicios Python,
 * aplicaciones móviles, etc.).</p>
 *
 * <p>El uso de {@link RestControllerAdvice} permite aplicar reglas globales
 * sin duplicar código en cada controlador. Todas las respuestas generadas
 * incluyen un timestamp, el código HTTP, un mensaje legible y la ruta donde
 * se produjo el error.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones del tipo {@link ResponseStatusException}.
     *
     * <p>Este tipo de excepción ya contiene un código de estado HTTP y un
     * mensaje, por lo que se reutilizan directamente para construir la
     * respuesta JSON.</p>
     *
     * @param ex  excepción lanzada.
     * @param req petición HTTP donde ocurrió el error.
     * @return respuesta JSON con el estado y mensaje correspondiente.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleRse(ResponseStatusException ex, HttpServletRequest req) {
        return error(ex.getStatusCode().value(), ex.getReason(), ex.getMessage(), req.getRequestURI());
    }

    /**
     * Maneja errores relacionados con el acceso a la base de datos.
     *
     * <p>Incluye excepciones como {@link DataAccessException}, que indican
     * fallos técnicos en la capa de persistencia.</p>
     *
     * @param ex  excepción lanzada.
     * @param req petición HTTP donde ocurrió el error.
     * @return respuesta JSON con código 500 y mensaje de error de base de datos.
     */
    @ExceptionHandler({ DataAccessException.class })
    public ResponseEntity<Map<String, Object>> handleDb(Exception ex, HttpServletRequest req) {
        return error(500, "Database Error", ex.getMessage(), req.getRequestURI());
    }

    /**
     * Maneja cualquier excepción no controlada específicamente por otros
     * métodos del manejador global.
     *
     * <p>Sirve como mecanismo de seguridad para evitar que errores inesperados
     * expongan información sensible o generen respuestas incoherentes.</p>
     *
     * @param ex  excepción lanzada.
     * @param req petición HTTP donde ocurrió el error.
     * @return respuesta JSON con código 500 y mensaje genérico.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAny(Exception ex, HttpServletRequest req) {
        return error(500, "Internal Error", ex.getMessage(), req.getRequestURI());
    }

    /**
     * Maneja errores de validación generados por Bean Validation.
     *
     * <p>Extrae los mensajes de error de cada campo inválido y los concatena
     * en un único mensaje legible para el cliente.</p>
     *
     * @param ex  excepción de validación.
     * @param req petición HTTP donde ocurrió el error.
     * @return respuesta JSON con código 400 y detalles de validación.
     */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            org.springframework.web.bind.MethodArgumentNotValidException ex,
            HttpServletRequest req) {

        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(java.util.stream.Collectors.joining(", "));

        return error(400, "Validation Failed", details, req.getRequestURI());
    }

    /**
     * Maneja errores cuando no existe un controlador para la ruta solicitada.
     *
     * <p>Equivale a un error HTTP 404 tradicional.</p>
     *
     * @param ex  excepción lanzada cuando no se encuentra un handler.
     * @param req petición HTTP donde ocurrió el error.
     * @return respuesta JSON con código 404.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            NoHandlerFoundException ex, HttpServletRequest req) {
        return error(404, "Not Found", "The requested resource does not exist", req.getRequestURI());
    }

    /**
     * Construye una respuesta JSON uniforme para cualquier tipo de error.
     *
     * <p>Incluye timestamp, código HTTP, mensaje, descripción y la ruta donde
     * ocurrió el error.</p>
     *
     * @param status  código HTTP.
     * @param error   tipo de error.
     * @param message mensaje detallado.
     * @param path    ruta de la petición.
     * @return respuesta HTTP con cuerpo JSON.
     */
    private ResponseEntity<Map<String, Object>> error(int status, String error, String message, String path) {
        Map<String, Object> body = new java.util.LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status);
        body.put("error", error);
        body.put("message", message == null ? "" : message);
        body.put("path", path);

        return ResponseEntity.status(status).body(body);
    }
}
