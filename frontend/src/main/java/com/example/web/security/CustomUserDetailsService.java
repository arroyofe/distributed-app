package com.example.web.security;

import com.example.web.models.User;
import com.example.web.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio de seguridad encargado de la carga de perfiles de usuario.
 * <p>
 * Implementa la interfaz {@link UserDetailsService} para integrar la lógica de
 * búsqueda de usuarios de nuestra base de datos MySQL con el motor de
 * autenticación de Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    /**
     * Inyección del repositorio de usuarios.
     * @param repo Repositorio para acceder a la tabla de usuarios.
     */
    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * Recupera un usuario por su 'username' y lo envuelve en un adaptador de seguridad.
     * <p>
     * Este método es invocado automáticamente por Spring Security durante el proceso
     * de login para verificar las credenciales.
     *
     * @param username El nombre de usuario introducido en el formulario de login.
     * @return Una instancia de {@link CustomUserDetails} con la información del usuario.
     * @throws UsernameNotFoundException Si el usuario no existe en la base de datos.
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        return new CustomUserDetails(user);
    }
}
