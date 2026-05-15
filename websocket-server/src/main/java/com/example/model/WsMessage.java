package com.example.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Entidad que representa un mensaje enviado a través del sistema WebSocket.
 * <p>
 * Los mensajes pueden ser broadcast o privados, y se almacenan en una cola
 * persistente hasta que el cliente los recibe. Esta entidad permite gestionar
 * el estado de entrega y los metadatos asociados.
 * <p>
 * La tabla asociada es {@code ws_message_queue}.
 */
@Entity
@Table(name = "ws_message_queue")
public class WsMessage {

    /**
     * Identificador único del mensaje.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del usuario que envió el mensaje.
     */
    @Column(nullable = false, length = 100)
    private String senderUsername;

    /**
     * Nombre del usuario destinatario del mensaje.
     */
    @Column(nullable = false, length = 100)
    private String receiverUsername;

    /**
     * Asunto del mensaje.
     */
    @Column(nullable = false, length = 255)
    private String subject;

    /**
     * Contenido principal del mensaje.
     * <p>
     * Se almacena como un campo de texto largo debido a su posible tamaño.
     */
    @Lob
    @Column(nullable = false)
    private String body;

    /**
     * Indica si el mensaje ya fue entregado al usuario destino.
     */
    @Column(nullable = false)
    private boolean delivered = false;

    /**
     * Fecha y hora en que el mensaje fue creado.
     * <p>
     * Se establece automáticamente antes de persistir.
     */
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    /**
     * Fecha y hora en que el mensaje fue marcado como entregado.
     */
    private Instant deliveredAt;

    /**
     * Constructor vacío requerido por JPA.
     */
    public WsMessage() {}

    /**
     * Establece la fecha de creación antes de insertar el registro.
     */
    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSenderUsername() { return senderUsername; }
    public void setSenderUsername(String senderUsername) { this.senderUsername = senderUsername; }

    public String getReceiverUsername() { return receiverUsername; }
    public void setReceiverUsername(String receiverUsername) { this.receiverUsername = receiverUsername; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public boolean isDelivered() { return delivered; }
    public void setDelivered(boolean delivered) { this.delivered = delivered; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(Instant deliveredAt) { this.deliveredAt = deliveredAt; }
}
