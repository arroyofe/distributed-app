package com.example.web.security;

import com.example.web.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

/**
 * Adaptador de seguridad (Wrapper) que implementa {@link UserDetails}.
 * <p>
 * Esta clase permite que Spring Security utilice nuestra entidad {@link User} personalizada
 * para los procesos de autenticación y autorización. Al ser un {@code record}, es inmutable
 * y facilita el acceso a los datos del usuario autenticado en la sesión.
 *
 * @param user La entidad de usuario proveniente de la base de datos MySQL.
 */
public record CustomUserDetails(User user) implements UserDetails {

    /**
     * Define los permisos del usuario basándose en su rol.
     * <p>
     * Aplica la convención de Spring Security añadiendo el prefijo "ROLE_"
     * si no está presente en la base de datos, garantizando la compatibilidad
     * con las anotaciones {@code @PreAuthorize}.
     *
     * @return Una colección que contiene la autoridad (rol) del usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole().startsWith("ROLE_") ? user.getRole() : "ROLE_" + user.getRole();
        return List.of(new SimpleGrantedAuthority(role));
    }

    /** @return El hash de la contraseña almacenado en la entidad. */
    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    /** @return El nombre de usuario único de la entidad. */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
