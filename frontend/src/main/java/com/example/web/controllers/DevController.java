package com.example.web.controllers;

import com.example.web.services.DbHealthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("hasAnyRole('DEV','ADMIN')")
public class DevController {

    private final DbHealthService dbHealthService;

    public DevController(DbHealthService dbHealthService) {
        this.dbHealthService = dbHealthService;
    }

    @GetMapping("/dev/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageName", "Espace Développeur");
        return "dev-dashboard";
    }

    @GetMapping("/dev/tools")
    public String tools(Model model) {

        boolean dbUp = dbHealthService.isDatabaseUp();
        String dbVersion = dbHealthService.getDbVersion();

        model.addAttribute("dbUp", dbUp);
        model.addAttribute("dbVersion", dbVersion);
        model.addAttribute("pageName", "Dev Tools");

        return "dev-tools";
    }
}