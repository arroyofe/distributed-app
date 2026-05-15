package com.example.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar la presencia de usuarios conectados
 * al sistema WebSocket.
 *
 * <p>Utiliza el {@link SimpUserRegistry} de Spring para obtener la lista
 * de usuarios activos en tiempo real. Este servicio es útil para mostrar
 * usuarios conectados, implementar indicadores de presencia o depurar
 * el estado del sistema.</p>
 */
@Service
public class PresenceService {

    private static final Logger log = LoggerFactory.getLogger(PresenceService.class);

    /**
     * Registro interno de usuarios conectados gestionado por Spring.
     */
    private final SimpUserRegistry userRegistry;

    /**
     * Crea una nueva instancia del servicio de presencia.
     *
     * @param userRegistry registro de usuarios WebSocket proporcionado por Spring.
     */
    public PresenceService(SimpUserRegistry userRegistry) {
        this.userRegistry = userRegistry;
    }

    /**
     * Obtiene el conjunto de nombres de usuarios actualmente conectados.
     *
     * <p>La información se obtiene directamente del registro de usuarios
     * WebSocket. También se registra en el log para facilitar la depuración.</p>
     *
     * @return conjunto de nombres de usuarios conectados.
     */
    public Set<String> getConnectedUsers() {
        Set<String> users = userRegistry.getUsers().stream()
                .map(u -> u.getName())
                .collect(Collectors.toSet());

        log.info(">>>> SimpUserRegistry detecta {} usuarios: {}", users.size(), users);

        return users;
    }
}
