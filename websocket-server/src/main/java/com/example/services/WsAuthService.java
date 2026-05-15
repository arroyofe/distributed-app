package com.example.services;

import com.example.model.WsUser;
import com.example.repository.WsUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de gestionar la lógica de autenticación y registro
 * de usuarios del sistema WebSocket.
 *
 * <p>Este servicio encapsula la validación de existencia de usuarios,
 * el proceso de codificación de contraseñas y la persistencia de nuevos
 * registros en la base de datos.</p>
 */
@Service
public class WsAuthService {

    /**
     * Repositorio encargado de la persistencia de usuarios.
     */
    private final WsUserRepository repo;

    /**
     * Codificador utilizado para almacenar contraseñas de forma segura.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Crea una nueva instancia del servicio de autenticación.
     *
     * @param repo repositorio de usuarios.
     * @param passwordEncoder codificador de contraseñas.
     */
    public WsAuthService(WsUserRepository repo,
                         PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * <p>Valida que el nombre de usuario no exista previamente. Si es válido,
     * crea un nuevo usuario con la contraseña codificada y lo almacena en la
     * base de datos.</p>
     *
     * @param username nombre de usuario solicitado.
     * @param rawPassword contraseña en texto plano enviada por el cliente.
     * @throws IllegalStateException si el usuario ya existe.
     */
    public void register(String username, String rawPassword) {
        if (repo.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Usuario ya existe");
        }

        WsUser user = new WsUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));

        repo.save(user);
    }
}
