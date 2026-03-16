package com.example.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class Py2PageController {

    private final RestTemplate rest = new RestTemplate();

    @Value("${app.backendBaseUrl:http://backend:8080}")
    private String backendBaseUrl;

    @GetMapping("/api2") // lien du menu
    public String api2(Model model) {
        try {
            Map resp = rest.getForObject(backendBaseUrl + "/api/py2/hello", Map.class);
            model.addAttribute("result", resp);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "api2"; // templates/api2.html
    }
}
