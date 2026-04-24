package com.example.app.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * Controlador de Hello. 
 */
@RestController
public class HelloController {
    @GetMapping("/api/hello")
    public Map<String, Object> hello() {
        return Map.of("message", "Hello from backend");
    }
}