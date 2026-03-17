package com.example.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class Py2ItemService {

    private final WebClient web;

    public Py2ItemService(WebClient py2WebClient) {
        this.web = py2WebClient;
    }

    public List<Map> getItems() {
        return web.get()
                .uri("/items")
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();
    }

    public Map<String, Object> addItem(Map<String, Object> payload) {
        return web.post()
                .uri("/items")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public void deleteItem(long id) {
        web.delete()
                .uri("/items/{id}", id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}