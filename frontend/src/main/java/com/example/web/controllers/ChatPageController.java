package com.example.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // ¡Importación clave!
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador encargado de gestionar las vistas relacionadas con el sistema de chat.
 * <p>
 * Proporciona las rutas necesarias para acceder a las páginas de registro,
 * inicio de sesión y sala de chat. Cada método añade al modelo información
 * básica utilizada por las plantillas Thymeleaf para mostrar títulos y nombres
 * de página.
 * <p>
 * Este controlador no maneja lógica de negocio ni autenticación; únicamente
 * sirve como puente entre las rutas HTTP y las vistas HTML correspondientes.
 *
 */
@Controller
@RequestMapping("/chat")
public class ChatPageController {

    /**
     * Muestra la página de registro del chat.
     * <p>
     * Añade al modelo el título y nombre de la página para ser utilizados
     * por la plantilla {@code register.html}.
     *
     * @param model modelo utilizado para pasar atributos a la vista.
     * @return el nombre de la plantilla Thymeleaf {@code register}.
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("pageTitle", "UBU - Registro de Chat");
        model.addAttribute("pageName", "Registro Chat");
        return "register";
    }

    /**
     * Muestra la página de acceso al chat.
     * <p>
     * Incluye atributos en el modelo para personalizar la vista
     * {@code chatlogin.html}.
     *
     * @param model modelo utilizado para pasar atributos a la vista.
     * @return el nombre de la plantilla Thymeleaf {@code chatlogin}.
     */
    @GetMapping("/chatlogin")
    public String chatlogin(Model model) {
        model.addAttribute("pageTitle", "UBU - Acceso al Chat");
        model.addAttribute("pageName", "Acceso Chat");
        return "chatlogin";
    }

    /**
     * Muestra la sala principal del chat.
     * <p>
     * Esta vista corresponde a la página donde los usuarios interactúan
     * mediante WebSockets una vez conectados.
     *
     * @param model modelo utilizado para pasar atributos a la vista.
     * @return el nombre de la plantilla Thymeleaf {@code chat}.
     */
    @GetMapping
    public String chat(Model model) {
        model.addAttribute("pageTitle", "UBU - Sala de Chat");
        model.addAttribute("pageName", "Chat Activo");
        return "chat";
    }
}
