package com.example.web.controllers;

import com.example.web.dto.UserDto;
import com.example.web.models.User;
import com.example.web.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador administrativo para la gestión de usuarios y roles.
 * <p>
 * Esta clase permite administrar las cuentas de acceso al sistema. Por motivos de seguridad,
 * toda la clase está restringida exclusivamente a usuarios con el rol 'ADMIN'.
 * Utiliza {@link UserService} para las operaciones de persistencia y encriptación.
 */
@Controller
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private static final String REDIR_US = "redirect:/users";
    private final UserService userService;
    /**
     * Inyección del servicio de usuarios.
     * @param userService Servicio que contiene la lógica de negocio y seguridad de usuarios.
     */
    public UserController(UserService userService) {

        this.userService = userService;
    }

    /**
     * Recupera y muestra la lista de todos los usuarios registrados.
     *
     * @param model Modelo para enviar la colección de usuarios a la vista.
     * @return El template "users".
     */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("pageName", "Usuarios");
        return "users";
    }

    /**
     * Muestra el formulario para registrar un nuevo usuario.
     *
     * @param model Modelo que inicializa un {@link UserDto} vacío.
     * @return El template "user-new".
     */
    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new UserDto());
        model.addAttribute("pageName", "Créer un utilisateur");
        return "user-new";
    }

    /**
     * Procesa la creación de un nuevo usuario con validación de datos.
     *
     * @param dto Datos del usuario capturados del formulario.
     * @param result Objeto que contiene los errores de validación, si los hay.
     * @return Redirección a la lista de usuarios si tiene éxito, o vuelve al formulario si hay errores.
     */
    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("user") UserDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "user-new";
        }

        userService.createUser(dto);
        return REDIR_US;

    }

    /**
     * Elimina una cuenta de usuario del sistema.
     *
     * @param id Identificador único del usuario a eliminar.
     * @return Redirección a la lista de usuarios.
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteById(id);
        return REDIR_US;
    }

    /**
     * Prepara el formulario para editar un usuario existente.
     * <p>
     * Nota: Por seguridad, el campo de contraseña se mantiene vacío en el DTO
     * al cargar el formulario.
     *
     * @param id Identificador del usuario a modificar.
     * @param model Modelo con los datos del usuario convertidos a DTO.
     * @return El template "user-edit".
     */
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

    /**
     * Procesa la actualización de la información de un usuario.
     *
     * @param id Identificador del usuario.
     * @param dto Objeto con los datos actualizados.
     * @return Redirección a la lista de usuarios.
     */
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("user") UserDto dto) {
        userService.updateUser(id, dto);
        return REDIR_US;
    }
}