package com.example.app.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;

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