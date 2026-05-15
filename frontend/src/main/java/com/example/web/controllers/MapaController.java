package com.example.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

/**
 * Controlador encargado de gestionar la vista del visor de coordenadas GPS.
 *
 * <p>Proporciona la ruta necesaria para acceder a la página del mapa interactivo,
 * donde se muestran coordenadas o información geográfica al usuario.</p>
 *
 * <p>Este controlador únicamente prepara los atributos básicos del modelo
 * utilizados por la plantilla Thymeleaf correspondiente.</p>
 */
@Controller
public class MapaController {

    /**
     * Muestra la página del mapa interactivo GPS.
     * <p>
     * Añade al modelo el título y nombre de la página para ser utilizados
     * por la plantilla {@code mapa.html}.
     *
     * @param model modelo utilizado para pasar atributos a la vista.
     * @return el nombre de la plantilla Thymeleaf {@code mapa}.
     */
    @GetMapping("/mapa")
    public String mostrarMapa(Model model) {
        model.addAttribute("pageTitle", "UBU - Visor de Coordenadas GPS");
        model.addAttribute("pageName", "Mapa Interactivo GPS");
        return "mapa";
    }
}
