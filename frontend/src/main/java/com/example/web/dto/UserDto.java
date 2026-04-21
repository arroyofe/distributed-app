package com.example.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Objeto de transferencia de datos para la gestión de cuentas de usuario.
 * <p>
 * Este DTO transporta la información necesaria para el registro y edición de usuarios,
 * incluyendo las restricciones de formato para el correo electrónico y la
 * obligatoriedad de los campos de seguridad.
 */
@Getter
@Setter
public class UserDto {

    /** Nombre de usuario único para el inicio de sesión. No puede estar en blanco. */
    @NotBlank
    private String username;

    /** Contraseña del usuario. En procesos de edición, puede ser tratada de forma opcional o encriptada. */
    @NotBlank
    private String password;

    /** Dirección de correo electrónico validada bajo el estándar RFC 5322. */
    @Email
    private String email;

    /** Rol asignado al usuario (ej. ROLE_USER, ROLE_ADMIN) para el control de acceso de Spring Security. */
    @NotBlank
    private String role;
}



