package com.example.security;

import com.example.model.WsUser;
import com.example.repository.WsUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementación personalizada de {@link UserDetailsService} para la
 * autenticación de usuarios del sistema WebSocket.
 *
 * <p>Este servicio permite a Spring Security cargar usuarios desde la base
 * de datos utilizando el repositorio {@link WsUserRepository}. La información
 * obtenida se transforma en un objeto {@link UserDetails} compatible con
 * el sistema de autenticación de Spring.</p>
 */
@Service
public class WsUserDetailsService implements UserDetailsService {

    /**
     * Repositorio encargado de la persistencia de usuarios.
     */
    private final WsUserRepository repo;

    /**
     * Crea una nueva instancia del servicio de detalles de usuario.
     *
     * @param repo repositorio de usuarios.
     */
    public WsUserDetailsService(WsUserRepository repo) {
        this.repo = repo;
    }

    /**
     * Carga un usuario por su nombre de usuario.
     *
     * <p>Si el usuario existe, se construye un objeto {@link UserDetails}
     * con su contraseña codificada y su estado de habilitación. Si no existe,
     * se lanza una excepción {@link UsernameNotFoundException}.</p>
     *
     * @param username nombre del usuario a buscar.
     * @return detalles del usuario para Spring Security.
     * @throws UsernameNotFoundException si el usuario no existe.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WsUser u = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return User.withUsername(u.getUsername())
                .password(u.getPassword())
                .roles("WS_USER")
                .disabled(!u.isEnabled())
                .build();
    }
}
