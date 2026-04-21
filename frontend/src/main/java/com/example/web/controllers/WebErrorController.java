package com.example.web.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador de errores personalizado para la aplicación web.
 * <p>
 * Implementa {@link ErrorController} para capturar y gestionar las excepciones
 * y errores HTTP (como 404, 500), proporcionando una experiencia de usuario
 * consistente mediante una vista personalizada.
 */
@Controller
public class WebErrorController implements ErrorController {

    /**
     * Gestiona las peticiones de error enviadas por el contenedor de servlets.
     * <p>
     * Extrae el código de estado HTTP y el mensaje de error de los atributos
     * de la solicitud para mostrarlos dinámicamente en la interfaz.
     *
     * @param request La solicitud HTTP que contiene los detalles del error.
     * @param model   Modelo de UI para pasar el código y mensaje a la vista.
     * @return El nombre del template "error/error-view".
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        model.addAttribute("code", status);
        model.addAttribute("msg", message != null ? message : "Erreur inconnue");

        return "error/error-view"; // Corresponde a templates/error/error-view.html
    }

    /**
     * Define la ruta de acceso a la gestión de errores.
     * <p>
     * Nota: Este método desactiva el BasicErrorController estándar de Spring Boot
     * para utilizar esta implementación personalizada.
     *
     * @return La cadena "/error".
     */
    public String getErrorPath() {

        return "/error";
    }
}