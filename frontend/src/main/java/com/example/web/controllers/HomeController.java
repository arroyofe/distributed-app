package com.example.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    // La raiz y /home muestran la página inicial
    @GetMapping
    public String home() {
        return "home";
    } // Busca en templates/home.html
}
