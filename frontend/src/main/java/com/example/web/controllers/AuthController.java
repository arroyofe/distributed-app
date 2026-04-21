package com.example.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador que gestiona los flujos de autentificación  y las respuestas de estado de sesión.
 * <p>
 * Esta clase realiza el interfaz con Spring Security para mostrar las páginas ligadas a la
 * conexion y a la desconexión de los usuarios.
 */
@Controller
public class AuthController {

    /**
     * Muestra la pógina de confirmación tras una desconexión efectuada con éxito.
     *
     * @return path lógico de la vista Thymeleaf "auth/logout-success"
     */
    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "auth/logout-success";
    }
}