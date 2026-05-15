package com.example.repository;

import com.example.model.WsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para gestionar la persistencia de usuarios del sistema WebSocket.
 *
 * <p>Proporciona operaciones CRUD estándar mediante {@link JpaRepository}
 * y métodos personalizados para la búsqueda de usuarios por nombre.</p>
 *
 * <p>La entidad gestionada es {@link com.example.model.WsUser}.</p>
 */
@Repository
public interface WsUserRepository extends JpaRepository<WsUser, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * <p>Este método se utiliza durante el proceso de autenticación
     * para validar credenciales.</p>
     *
     * @param username nombre del usuario a buscar.
     * @return un {@link Optional} que contiene el usuario si existe.
     */
    Optional<WsUser> findByUsername(String username);
}
