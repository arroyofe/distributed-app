package com.example.web.repositories;

import com.example.web.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio para la gestión de persistencia de la entidad {@link User}.
 * <p>
 * Proporciona los métodos estándar de CRUD gracias a la herencia de JpaRepository.
 * Esta interfaz es utilizada por Spring Security para localizar usuarios durante el login.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario en la base de datos basándose en su nombre de usuario.
     *
     * @param username El nombre de cuenta del usuario.
     * @return Un {@link Optional} que contiene el usuario si se encuentra, o vacío si no existe.
     */
    Optional<User> findByUsername(String username);
}
