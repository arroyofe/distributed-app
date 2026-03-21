package com.example.web.controllers;

import com.example.web.models.User;
import com.example.web.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", service.findAll());
        model.addAttribute("pageName", "Gestión usuarios");
        return "admin-users";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageName", "Nuevo usuario");
        return "admin-user-new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute User user) {
        service.create(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin/users";
    }
}