package com.example.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

/**
 * Controlador de integración para el microservicio Python 2 (py2).
 * <p>
 * Gestiona la comunicación y visualización de datos provenientes del segundo
 * módulo Python, sirviendo como enlace entre la interfaz de usuario Java
 * y la lógica externa del backend.
 */
@Controller
public class Py2PageController {

    private final RestTemplate rest = new RestTemplate();

    /**
     * URL base del backend/microservicio, inyectada desde la configuración.
     * Por defecto intenta conectar con el contenedor o servicio 'backend' en el puerto 8080.
     */
    @Value("${app.backendBaseUrl:http://backend:8080}")
    private String backendBaseUrl;

    /**
     * Recupera un mensaje de bienvenida o estado desde el servicio py2.
     * <p>
     * Realiza una petición GET al endpoint de saludo de py2 y mapea la respuesta
     * JSON en un objeto de tipo Map para su representación en la vista.
     * En caso de fallo en la comunicación, captura la excepción para mostrar el error al usuario.
     *
     * @param model Modelo de UI para enviar los datos de respuesta o el mensaje de error.
     * @return El nombre del template "api2".
     */
    @GetMapping("/api2")
    public String api2(Model model) {
        try {
            Map<String, Object> resp = rest.getForObject(
                    backendBaseUrl + "/api/py2/hello",
                    Map.class
            );
            model.addAttribute("result", resp);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "api2";
    }
}
