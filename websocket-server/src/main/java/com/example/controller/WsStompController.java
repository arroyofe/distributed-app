package com.example.controller;


import com.example.dto.ChatMessage;
import com.example.dto.PrivateChatMessage;
import com.example.services.WsMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.Principal;


/**
 * Controlador encargado de gestionar los mensajes enviados a través de STOMP
 * sobre WebSockets.
 * <p>
 * Define los endpoints de mensajería en tiempo real utilizados por los
 * clientes conectados mediante WebSocket. Permite enviar mensajes broadcast
 * a todos los usuarios y mensajes privados dirigidos a un usuario específico.
 * <p>
 * Este controlador no expone rutas HTTP; únicamente maneja mensajes STOMP
 * enviados desde el cliente mediante {@code /app/...}.
 */
@Controller
public class WsStompController {

    private static final String WARNLOG = "Principal es null para el mensaje";

    /**
     * Servicio encargado de gestionar el envío y almacenamiento de mensajes.
     */
    private final WsMessageService messageService;

    /**
     * Logger para registrar eventos relevantes del sistema de mensajería.
     */
    private static final Logger logger = LoggerFactory.getLogger(WsStompController.class);

    /**
     * Crea una nueva instancia del controlador STOMP.
     *
     * @param messageService servicio de gestión de mensajes.
     */
    public WsStompController(WsMessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Maneja mensajes broadcast enviados por un usuario.
     * <p>
     * Los mensajes enviados a {@code /app/broadcast} son distribuidos a todos
     * los usuarios conectados. El método valida que exista un {@link Principal}
     * asociado a la conexión WebSocket.
     *
     * @param msg mensaje enviado por el cliente.
     * @param principal usuario autenticado asociado al WebSocket.
     */
    @MessageMapping("/broadcast")
    public void broadcast(@Payload ChatMessage msg, Principal principal) {
        if (principal == null) {
            logger.warn(WARNLOG);
            return;
        }

        messageService.sendToAll(principal.getName(), msg.getSubject(), msg.getBody());

        // Log de prueba
        System.out.println(">>> BROADCAST de " + principal.getName()
                + " | asunto=" + msg.getSubject()
                + " | body=" + msg.getBody());
    }

    /**
     * Maneja mensajes privados enviados por un usuario.
     * <p>
     * Los mensajes enviados a {@code /app/private} se entregan únicamente
     * al usuario destino especificado en el mensaje. El método valida que
     * exista un {@link Principal} asociado a la conexión WebSocket.
     *
     * @param msg mensaje privado enviado por el cliente.
     * @param principal usuario autenticado asociado al WebSocket.
     */
    @MessageMapping("/private")
    public void privateMessage(@Payload PrivateChatMessage msg, Principal principal) {
        if (principal == null) {
            logger.warn(WARNLOG);
            return;
        }

        messageService.sendToUser(
                principal.getName(),
                msg.getTo(),
                msg.getSubject(),
                msg.getBody()
        );

        // Log de prueba
        System.out.println(">>> PRIVADO de " + principal.getName()
                + " a " + msg.getTo());
    }
}
