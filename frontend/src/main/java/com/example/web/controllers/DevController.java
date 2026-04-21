package com.example.web.controllers;

import com.example.web.services.DbHealthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador reservado a la gestión de acesos technicos y de mantenimiento.
 * <p>
 * Provee igualmente las herramientas de diagnóstico (todavía en desarrollo) y un cuadro de mando
 * para los perfiles privilegiados (también en desarrollo).
 * El acceso está restringico a los usuarion que posena los roles de DEV o ADMIN usando Spring Security.
 */
@Controller
@PreAuthorize("hasAnyRole('DEV','ADMIN')")
public class DevController {

    private final DbHealthService dbHealthService;

    /**
     * Constructor para la inyección de dependenciass.
     *
     * @param dbHealthService Servicio que gestiona el diagnóstico del estado de la base de datos.
     */
    public DevController(DbHealthService dbHealthService) {
        this.dbHealthService = dbHealthService;
    }

    /**
     * Redirige al usuario hacia el cuadro de mando "desarrollador".
     *
     * @return una instrucción de redirección hacia la URL /dev/dashboard.
     */
    @GetMapping("/dev")
    public String root() {
        return "redirect:/dev/dashboard";
    }

    /**
     * Muestra la pógina de acogida del espacio "desarrollador".
     *
     * @param model Objeto Model para pasar el nombre de la página a la vista Thymeleaf.
     * @return el nombre del template "dev-dashboard".
     */
    @GetMapping("/dev/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageName", "Espacio Desarrollador");
        return "dev-dashboard";
    }

    /**
     * Muestra las herramientas disponibles del diagnostico técnico (Ya disponible el
     * estado y la versión de la base de datos).
     *
     * @param model Objeto Model enriquecido con los indicadores de "salud" del sistema.
     * @return el nombre del template "dev-tools".
     */
    @GetMapping("/dev/tools")
    public String tools(Model model) {

        model.addAttribute("dbUp", dbHealthService.isDatabaseUp());
        model.addAttribute("dbVersion", dbHealthService.getDbVersion());
        model.addAttribute("pageName", "Dev Tools");

        return "dev-tools";
    }
}