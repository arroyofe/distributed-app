package com.example.services;

import com.example.model.WsMessage;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import com.example.repository.WsMessageRepository;

import java.time.Instant;
import java.util.List;

/**
 * Servicio encargado de gestionar el envío, almacenamiento y entrega de mensajes
 * dentro del sistema WebSocket.
 *
 * <p>Este servicio centraliza toda la lógica relacionada con:
 * <ul>
 *   <li>Mensajes broadcast enviados a todos los usuarios.</li>
 *   <li>Mensajes privados enviados a un usuario específico.</li>
 *   <li>Persistencia de mensajes no entregados (cola offline).</li>
 *   <li>Actualización del estado de entrega.</li>
 *   <li>Consulta de usuarios conectados mediante {@link SimpUserRegistry}.</li>
 * </ul>
 * </p>
 *
 * <p>Los mensajes se almacenan en la base de datos para garantizar que los
 * usuarios desconectados puedan recibirlos al reconectarse.</p>
 */
@Service
public class WsMessageService {

    private final WsMessageRepository repo;
    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry userRegistry;

    private static final Logger logger = LoggerFactory.getLogger(WsMessageService.class);

    /**
     * Crea una nueva instancia del servicio de mensajería WebSocket.
     *
     * @param repo repositorio encargado de la persistencia de mensajes.
     * @param messagingTemplate plantilla para enviar mensajes STOMP.
     * @param userRegistry registro de usuarios conectados vía WebSocket.
     */
    public WsMessageService(WsMessageRepository repo,
                            SimpMessagingTemplate messagingTemplate,
                            SimpUserRegistry userRegistry) {
        this.repo = repo;
        this.messagingTemplate = messagingTemplate;
        this.userRegistry = userRegistry;
    }

    // ---------------------------------------------------------
    // 1. Enviar mensaje a todos los usuarios conectados
    // ---------------------------------------------------------

    /**
     * Envía un mensaje broadcast a todos los usuarios conectados.
     *
     * <p>El mensaje se marca como entregado inmediatamente, ya que se envía
     * directamente al destino {@code /topic/broadcast}. También se almacena
     * en la base de datos para registro histórico.</p>
     *
     * @param sender nombre del usuario que envía el mensaje.
     * @param subject asunto del mensaje.
     * @param body contenido del mensaje.
     */
    public void sendToAll(String sender, String subject, String body) {

        WsMessage msg = new WsMessage();
        msg.setSenderUsername(sender);
        msg.setReceiverUsername("*"); // broadcast
        msg.setSubject(subject);
        msg.setBody(body);
        msg.setDelivered(true);
        msg.setDeliveredAt(Instant.now());

        repo.save(msg);

        messagingTemplate.convertAndSend("/topic/broadcast", msg);
    }

    // ---------------------------------------------------------
    // 2. Enviar mensaje privado a un usuario
    // ---------------------------------------------------------

    /**
     * Envía un mensaje privado a un usuario específico.
     *
     * <p>Si el usuario destino está conectado, el mensaje se envía
     * inmediatamente mediante {@code /user/{username}/queue/messages} y se
     * marca como entregado.</p>
     *
     * <p>Si el usuario está desconectado, el mensaje se almacena como
     * no entregado para que pueda recuperarlo posteriormente.</p>
     *
     * @param sender nombre del usuario que envía el mensaje.
     * @param receiver nombre del usuario destinatario.
     * @param subject asunto del mensaje.
     * @param body contenido del mensaje.
     */
    public void sendToUser(String sender, String receiver, String subject, String body) {

        boolean receiverConnected = isUserConnected(receiver);

        WsMessage msg = new WsMessage();
        msg.setSenderUsername(sender);
        msg.setReceiverUsername(receiver);
        msg.setSubject(subject);
        msg.setBody(body);

        if (receiverConnected) {
            msg.setDelivered(true);
            msg.setDeliveredAt(Instant.now());
            repo.save(msg);

            messagingTemplate.convertAndSendToUser(receiver, "/queue/messages", msg);

        } else {
            // queda en cola
            msg.setDelivered(false);
            repo.save(msg);
        }
    }

    // ---------------------------------------------------------
    // 3. Obtener mensajes pendientes (no entregados)
    // ---------------------------------------------------------

    /**
     * Obtiene todos los mensajes no entregados para un usuario.
     *
     * <p>Este método se utiliza cuando un usuario inicia sesión o se reconecta,
     * permitiendo recuperar mensajes almacenados en la cola offline.</p>
     *
     * @param username nombre del usuario destinatario.
     * @return lista de mensajes pendientes.
     */
    public List<WsMessage> getPendingMessages(String username) {
        return repo.findByReceiverUsernameAndDeliveredFalse(username);
    }

    // ---------------------------------------------------------
    // 4. Marcar mensajes como entregados
    // ---------------------------------------------------------

    /**
     * Marca una lista de mensajes como entregados.
     *
     * <p>Se actualiza el estado de entrega y la fecha correspondiente.
     * Este método se invoca normalmente desde el cliente REST cuando
     * confirma la recepción de mensajes pendientes.</p>
     *
     * @param messages lista de mensajes a actualizar.
     */
    public void markAsDelivered(List<WsMessage> messages) {
        Instant now = Instant.now();
        messages.forEach(m -> {
            m.setDelivered(true);
            m.setDeliveredAt(now);
        });
        repo.saveAll(messages);
    }

    // ---------------------------------------------------------
    // 5. Saber si un usuario está conectado
    // ---------------------------------------------------------

    /**
     * Verifica si un usuario está actualmente conectado al WebSocket.
     *
     * <p>La comprobación se realiza consultando el {@link SimpUserRegistry}
     * interno de Spring.</p>
     *
     * @param username nombre del usuario.
     * @return {@code true} si el usuario está conectado, {@code false} en caso contrario.
     */
    public boolean isUserConnected(String username) {
        return userRegistry.getUsers().stream()
                .anyMatch(u -> u.getName().equals(username));
    }

    /**
     * Obtiene un mensaje por su identificador.
     *
     * <p>Si el mensaje no existe, se lanza una excepción
     * {@link EntityNotFoundException} y se registra un aviso en el log.</p>
     *
     * @param id identificador del mensaje.
     * @return el mensaje correspondiente.
     * @throws EntityNotFoundException si no existe un mensaje con esa ID.
     */
    public WsMessage getMessageById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Message not found with id {}", id);
                    return new EntityNotFoundException("Mensaje no encontrado con la id: " + id);
                });
    }
}
