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
 * Gestor global de las excepciones para el conjunto del backend.
 *
 * Esta clase centraliza ele tratamiento de los errores non interceptados por los
 * controladores REST y garantiza una respuesta JSON coherente para todos los tipos
 * de excepciones usuales. <Permite la uniformizadión de la estructura de los mensajes
 * de error enviados al cliente, facilitando por lo tanto el debugg, la interpretación
 * y la integración con los clientes externos (frontend, servicios Python, etc.).
 *
 * Para ello transforma las excepciones estóndar de Srping en respuestas HTTP adaptadas,
 * gestiona los errores de validación Bean Validation, los errores técnicos (base de datos,
 * excepciones genéricas) construyendo un marco de respuesta uniforme que contiene un
 * timestamp, el código HTTP correspondiente, un mensaje legible y el camino de la
 * petición para identificar dónde se ha producido el error.
 *
 *
 * El uso de {@code @RestControllerAdvice} permite la aplicación de reglas uniformes y
 * globales a todos los controladores REST sin duplicar el código.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleRse(ResponseStatusException ex, HttpServletRequest req) {
        return error(ex.getStatusCode().value(), ex.getReason(), ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler({ DataAccessException.class })
    public ResponseEntity<Map<String, Object>> handleDb(Exception ex, HttpServletRequest req) {
        return error(500, "Database Error", ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAny(Exception ex, HttpServletRequest req) {
        return error(500, "Internal Error", ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(org.springframework.web.bind.MethodArgumentNotValidException ex, HttpServletRequest req) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(java.util.stream.Collectors.joining(", "));

        return error(400, "Validation Failed", details, req.getRequestURI());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            NoHandlerFoundException ex, HttpServletRequest req) {
        return error(404, "Not Found", "The requested resource does not exist", req.getRequestURI());
    }



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