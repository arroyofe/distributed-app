package com.example.controller;

import com.example.model.WsUser;
import com.example.repository.WsUserRepository;
import com.example.dto.WsRegisterRequest;
import com.example.dto.WsLoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST encargado de gestionar el registro y autenticación
 * de usuarios para el sistema de WebSockets.
 * <p>
 * Proporciona endpoints para registrar nuevos usuarios y validar
 * credenciales antes de permitir el acceso al chat en tiempo real.
 * <p>
 * Este controlador no genera tokens ni sesiones; simplemente valida
 * credenciales y delega el almacenamiento en el repositorio correspondiente.
 */
@RestController
public class WsAuthController {

    /**
     * Repositorio encargado de gestionar la persistencia de usuarios WebSocket.
     */
    private final WsUserRepository repo;

    /**
     * Codificador de contraseñas utilizado para almacenar hashes seguros.
     */
    private final PasswordEncoder encoder;

    /**
     * Crea una nueva instancia del controlador de autenticación WebSocket.
     *
     * @param repo repositorio de usuarios.
     * @param encoder codificador de contraseñas.
     */
    public WsAuthController(WsUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * <p>
     * Valida que el nombre de usuario no exista previamente. Si es válido,
     * crea un nuevo usuario con la contraseña codificada y lo almacena en la base de datos.
     *
     * @param req objeto con los datos de registro: {@code username} y {@code password}.
     * @return {@code 200 OK} si el usuario fue registrado correctamente,
     *         {@code 400 Bad Request} si el usuario ya existe.
     */
    @PostMapping("/ws-register")
    public ResponseEntity<?> register(@RequestBody WsRegisterRequest req) {

        if (repo.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }

        WsUser u = new WsUser();
        u.setUsername(req.getUsername());
        u.setPassword(encoder.encode(req.getPassword()));
        repo.save(u);

        return ResponseEntity.ok("Usuario registrado");
    }

    /**
     * Valida las credenciales de un usuario para permitir su acceso al chat.
     * <p>
     * Comprueba que el usuario exista y que la contraseña proporcionada
     * coincida con el hash almacenado. No genera tokens ni sesiones; simplemente
     * confirma si las credenciales son correctas.
     *
     * @param req objeto con los datos de inicio de sesión: {@code username} y {@code password}.
     * @return {@code 200 OK} si las credenciales son válidas,
     *         {@code 401 Unauthorized} si el usuario no existe o la contraseña es incorrecta.
     */
    @PostMapping(
            value = "/ws-login",
            consumes = "application/json"
    )
    public ResponseEntity<?> login(@RequestBody WsLoginRequest req) {

        WsUser user = repo.findByUsername(req.getUsername())
                .orElse(null);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Usuario no encontrado");
        }

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas");
        }

        return ResponseEntity.ok().build();
    }
}
