package com.example.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de acceso al sistema.
 * <p>
 * Gestiona exclusivamente la visualización del formulario de autenticación.
 * La lógica de validación y el filtrado de seguridad están delegados a la
 * configuración de Spring Security.
 */
@Controller
public class LoginController {


    /**
     * Sirve el formulario de inicio de sesión principal.
     *
     * @return El nombre lógico de la vista "login", asociada al archivo
     *         src/main/resources/templates/login.html.
     */
    @GetMapping("/login")
    public String login() {

        return "login"; // templates/login.html
    }
}
