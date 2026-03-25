package com.example.app.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RestController
@RequestMapping("/api/py2")
public class Py2ProxyController {

    private final WebClient py2WebClient;
    private final RestTemplate restTemplate;
    private final String py2BaseUrl;

    public Py2ProxyController(
            WebClient py2WebClient,
            RestTemplate restTemplate,
            @Value("${app.py2BaseUrl}") String py2BaseUrl) {

        this.py2WebClient = py2WebClient;
        this.restTemplate = restTemplate;
        this.py2BaseUrl = py2BaseUrl;
    }

    @GetMapping("/hello")
    public ResponseEntity<Map<String, Object>> hello() {
        try {
            // --- Version WebClient (bloqueante) ---
            Map<String, Object> body = py2WebClient.get()
                    .uri("/api/hello")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return ResponseEntity.ok(body);

        } catch (Exception webClientException) {
            // --- Fallback con RestTemplate ---
            try {
                ResponseEntity<Map> resp = restTemplate.getForEntity(
                        py2BaseUrl + "/api/hello",
                        Map.class
                );
                return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());

            } catch (RestClientException restEx) {
                return ResponseEntity.status(502).body(Map.of(
                        "status", 502,
                        "error", "Upstream Error",
                        "message", restEx.getMessage(),
                        "path", "/api/py2/hello"
                ));
            }
        }
    }
}