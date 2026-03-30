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
        model.addAttribute("pageName", "Usuarios");
        return "users";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new UserDto());
        model.addAttribute("pageName", "Créer un utilisateur");
        return "user-new";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("user") UserDto dto, BindingResult result) {
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

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id); // Il faudra créer findById dans le service
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        // On laisse le password vide dans le DTO pour la sécurité

        model.addAttribute("user", dto);
        model.addAttribute("userId", id);
        return "user-edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("user") UserDto dto) {
        userService.updateUser(id, dto);
        return "redirect:/users";
    }


}