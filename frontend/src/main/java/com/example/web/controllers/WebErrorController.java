package com.example.web.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        model.addAttribute("code", status);
        model.addAttribute("msg", message != null ? message : "Erreur inconnue");

        return "error/error-view"; // Corresponde a templates/error/error-view.html
    }

    // Esta instrucción desactiva BasicErrorController para evitar problemas de Springboot
    public String getErrorPath() {
        return "/error";
    }
}