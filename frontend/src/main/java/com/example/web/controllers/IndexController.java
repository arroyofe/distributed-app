package com.example.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class IndexController {
    // /index muestra la página /index (menú de opciones)
    @GetMapping
    public String index() {
        return "index";// Busca templates/index.html
    }
}
