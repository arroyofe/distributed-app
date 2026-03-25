package com.example.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "auth/logout"; // templates/auth/logout.html
    }
}