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

/**
 * Servicio de lógica de negocio para la gestión de usuarios.
 * <p>
 * Centraliza las operaciones CRUD de usuarios, garantizando la seguridad mediante
 * la encriptación de contraseñas y validaciones de integridad administrativa.
 */
@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    /**
     * Constructor con inyección de dependencias.
     * @param repo Repositorio de persistencia de usuarios.
     * @param encoder Componente para el cifrado seguro de contraseñas.
     */
    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    /** @return Lista de todos los usuarios registrados en el sistema. */
    public List<User> findAll() {
        return repo.findAll();
    }

    /**
     * Crea un nuevo usuario transformando el DTO en una entidad persistente.
     * <p>
     * La contraseña recibida en texto plano se codifica mediante {@link PasswordEncoder}
     * antes de ser almacenada como hash en la base de datos.
     *
     * @param dto Datos del nuevo usuario desde el formulario.
     */
    public void createUser(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setPasswordHash(encoder.encode(dto.getPassword()));
        repo.save(user);
    }

    /**
     * Elimina un usuario por su ID, con protección de seguridad activa.
     * <p>
     * Verifica que el administrador actualmente autenticado en el contexto de seguridad
     * no intente eliminarse a sí mismo para evitar el bloqueo del sistema.
     *
     * @param id Identificador del usuario a eliminar.
     * @throws ResponseStatusException con código 403 (FORBIDDEN) si se detecta auto-eliminación.
     */
    public void deleteById(Long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = ((UserDetails) principal).getUsername();

        User currentUser = repo.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Admin no encontrado"));

        if (currentUser.getId().equals(id)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Auto-eliminación prohibida : No puede suprimir su propia cuenta de admin.");
        }
        repo.deleteById(id);
    }

    /**
     * Localiza un usuario por su ID.
     * @param id Identificador único.
     * @return La entidad {@link User} encontrada.
     * @throws RuntimeException si el usuario no existe.
     */
    public User findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    /**
     * Actualiza los datos de un usuario existente.
     * <p>
     * Solo actualiza la contraseña si el campo recibido en el DTO no está vacío,
     * permitiendo modificar otros datos del perfil sin resetear las credenciales.
     *
     * @param id  ID del usuario a modificar.
     * @param dto Objeto con la información actualizada.
     */
    public void updateUser(Long id, UserDto dto) {
        User user = findById(id);
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            user.setPasswordHash(encoder.encode(dto.getPassword()));
        }

        repo.save(user);
    }
}
