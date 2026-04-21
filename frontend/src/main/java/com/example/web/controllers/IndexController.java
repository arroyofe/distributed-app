package com.example.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para la gestión de la ruta específica "/index".
 * <p>
 * Proporciona un punto de acceso explícito a la página principal de la aplicación,
 * complementando al controlador raíz.
 */
@Controller
@RequestMapping("/index")
public class IndexController {

    /**
     * Mapea las peticiones GET a la URL "/index"(menú de opciones).
     *
     * @return el nombre del template "index", correspondiente al archivo
     * fuente src/main/resources/templates/index.html.
     */
    @GetMapping
    public String index() {
        return "index";// Busca templates/index.html
    }
}
