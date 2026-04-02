package com.example.web.services;

import com.example.web.dto.UserDto;
import com.example.web.models.User;
import com.example.web.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    public void createUser(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        // En el DTO se escribe la contraseña en claro, y se llama a getPassword para crear el parswordHash
        // que se almacena en la base
        user.setPasswordHash(encoder.encode(dto.getPassword()));
        repo.save(user);
    }

    public void deleteById(Long id) {
        // Para evitar que el admin se borre a sí mismo por error
        // 1. Se recupera el username del admin que está actualemente conectadp
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = ((UserDetails) principal).getUsername();

        // 2. Se obtiene el objeto User correspondiente a dicho admin
        User currentUser = repo.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Admin no encontrado"));

        // 3. Se verifica si el admin actual intenta suprimirse a sí mismo
        if (currentUser.getId().equals(id)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Auto-eliminación prohibida : No puede suprimir su propia cuenta de admin.");

        }
        repo.deleteById(id);
    }

    public User findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void updateUser(Long id, UserDto dto) {
        User user = findById(id);
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

        // Se cambia la contraseña solamente si está vacía
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            user.setPasswordHash(encoder.encode(dto.getPassword()));
        }

        repo.save(user);
    }

}