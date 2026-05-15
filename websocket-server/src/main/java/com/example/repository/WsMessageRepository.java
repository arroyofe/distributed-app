package com.example.repository;

import com.example.model.WsMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para gestionar la persistencia de mensajes WebSocket.
 *
 * <p>Proporciona operaciones CRUD estándar mediante {@link JpaRepository}
 * y métodos personalizados para recuperar mensajes pendientes de entrega.</p>
 *
 * <p>La entidad gestionada es {@link com.example.model.WsMessage}.</p>
 */
@Repository
public interface WsMessageRepository extends JpaRepository<WsMessage, Long> {

    /**
     * Obtiene todos los mensajes no entregados destinados a un usuario específico.
     *
     * <p>Este método se utiliza para recuperar mensajes pendientes cuando
     * un usuario inicia sesión o se reconecta al WebSocket.</p>
     *
     * @param receiverUsername nombre del usuario destinatario.
     * @return lista de mensajes no entregados.
     */
    List<WsMessage> findByReceiverUsernameAndDeliveredFalse(String receiverUsername);
}
