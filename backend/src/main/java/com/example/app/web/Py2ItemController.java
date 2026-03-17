package com.example.app.web;

import com.example.app.service.Py2ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/py2/items")
public class Py2ItemController {

    private final Py2ItemService service;

    public Py2ItemController(Py2ItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<Map> list() {
        return service.getItems();
    }

    @PostMapping
    public Map<String, Object> create(@RequestBody Map<String,Object> body) {
        return service.addItem(body);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.deleteItem(id);
    }
}

