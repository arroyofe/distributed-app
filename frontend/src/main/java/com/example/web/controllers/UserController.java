package com.example.web.controllers;

import com.example.web.dto.UserDto;
import com.example.web.models.User;
import com.example.web.repositories.UserRepository;
import com.example.web.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("pageName", "Gestión de usuarios");
        return "users";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new UserDto());
        model.addAttribute("pageName", "Créer un utilisateur");
        return "user-new";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute ("user") UserDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "user-new";
        }

        userService.createUser(dto);
        return "redirect:/users";

    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}