package com.example.web.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entidad de persistencia que representa a un usuario en el sistema.
 * <p>
 * Esta clase está mapeada directamente con la tabla "users" de MySQL,
 * compartida con los módulos Python. Gestiona la identidad de los usuarios,
 * sus credenciales y sus permisos de acceso.
 */
@Data
@Entity
@Table(name = "users")
public class User {

    /** Identificador único autoincremental en la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de usuario único utilizado para la autenticación. */
    @Column(unique = true)
    private String username;

    /** Contraseña almacenada en formato hash (BCrypt u otro) por seguridad. */
    @Column(name = "password_hash")
    private String passwordHash;

    /** Dirección de correo electrónico del usuario. */
    private String email;

    /** Rol asignado (ej. ADMIN, DEV, USER) para el control de acceso. */
    private String role;

    /**
     * Fecha y hora de creación del registro.
     * Gestionada a nivel de base de datos (no modificable desde la aplicación).
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de la última actualización del registro.
     * Gestionada automáticamente por el motor de base de datos.
     * En el momento de la creación coincide c on la fecha created_at
     */
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

}
