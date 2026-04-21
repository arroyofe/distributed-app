package com.example.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador principal que gestiona el acceso a la página de inicio.
 * <p>
 * Sirve como punto de entrada público de la aplicación web.
 */
@Controller
@RequestMapping("/")
public class HomeController {
    /**
     * Gestiona las peticiones GET a la raíz de la aplicación. La raiz y /home
     * muestran la página inicial
     *
     * @return el nombre lógico de la vista "home", que Thymeleaf resolverá
     * como el archivo templates/home.html.
     */
    @GetMapping
    public String home() {
        return "home";
    } // Busca en templates/home.html
}
