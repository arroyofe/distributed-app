package com.example.websocket;

import com.example.model.WsMessage;
import com.example.services.WsMessageService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent; // <-- CAMBIO AQUÍ

import java.security.Principal;
import java.util.List;

/**
 * Listener encargado de gestionar eventos de conexión WebSocket.
 *
 * <p>Este componente escucha el evento {@link SessionConnectedEvent}, el cual
 * garantiza que el usuario ya ha sido autenticado y la sesión STOMP está lista.</p>
 *
 * <p>Su función principal es entregar mensajes pendientes (offline) al usuario
 * que acaba de conectarse, utilizando el servicio {@link WsMessageService}.</p>
 */
@Component
public class WebSocketConnectListener {

    private final WsMessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Crea una nueva instancia del listener de conexiones WebSocket.
     *
     * @param messageService servicio de gestión de mensajes.
     * @param messagingTemplate plantilla para enviar mensajes STOMP.
     */
    public WebSocketConnectListener(WsMessageService messageService,
                                    SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Maneja el evento de conexión WebSocket.
     *
     * <p>Cuando un usuario se conecta, se buscan sus mensajes pendientes
     * y se envían a través del canal privado {@code /user/queue/messages}.
     * Después, se marcan como entregados.</p>
     *
     * <p>Se introduce un pequeño retardo para permitir que el cliente
     * JavaScript complete sus suscripciones STOMP.</p>
     *
     * @param event evento de conexión STOMP.
     */
    @EventListener
    public void handleWebSocketConnected(SessionConnectedEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = accessor.getUser();

        if (user == null) {
            System.out.println(">>> Listener: Usuario no identificado aún.");
            return;
        }

        String username = user.getName();
        System.out.println(">>> Usuario conectado: " + username + ". Buscando mensajes pendientes...");

        new Thread(() -> {
            try {
                Thread.sleep(500); // Permite que el cliente se suscriba

                List<WsMessage> pending = messageService.getPendingMessages(username);

                if (!pending.isEmpty()) {
                    System.out.println(">>> Entregando " + pending.size() + " mensajes offline a " + username);

                    pending.forEach(msg ->
                            messagingTemplate.convertAndSendToUser(
                                    username, "/queue/messages", msg)
                    );

                    messageService.markAsDelivered(pending);
                } else {
                    System.out.println(">>> No hay mensajes pendientes para " + username);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
