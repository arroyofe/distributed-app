package com.example.websocket;

import com.example.services.PresenceService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Set;

/**
 * Programador encargado de enviar periódicamente la lista de usuarios conectados
 * a todos los clientes suscritos al canal WebSocket correspondiente.
 *
 * <p>Este componente consulta el {@link PresenceService} cada pocos segundos
 * y, si la lista de usuarios ha cambiado desde la última emisión, la envía
 * mediante {@link SimpMessagingTemplate} al destino {@code /topic/connected-users}.</p>
 *
 * <p>El objetivo es mantener a los clientes actualizados sin generar tráfico
 * innecesario en la red.</p>
 */
@Component
public class PresenceScheduler {

    private final SimpMessagingTemplate messagingTemplate;
    private final PresenceService presenceService;

    /**
     * Última lista enviada, utilizada para evitar reenvíos innecesarios.
     */
    private Set<String> lastSentUsers = Set.of();

    /**
     * Crea una nueva instancia del programador de presencia.
     *
     * @param messagingTemplate plantilla para enviar mensajes STOMP.
     * @param presenceService servicio que obtiene los usuarios conectados.
     */
    public PresenceScheduler(SimpMessagingTemplate messagingTemplate, PresenceService presenceService) {
        this.messagingTemplate = messagingTemplate;
        this.presenceService = presenceService;
    }

    /**
     * Envía la lista de usuarios conectados cada 3 segundos.
     *
     * <p>Solo se envía si la lista ha cambiado respecto a la última emisión,
     * optimizando así el uso de ancho de banda.</p>
     */
    @Scheduled(fixedDelay = 3000)
    public void broadcastUserList() {
        Set<String> currentUsers = presenceService.getConnectedUsers();

        if (!currentUsers.equals(lastSentUsers)) {
            messagingTemplate.convertAndSend("/topic/connected-users", currentUsers);
            lastSentUsers = currentUsers;
        }
    }
}
