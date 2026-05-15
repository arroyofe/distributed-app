package com.example.dto;

/**
 * DTO utilizado para representar un mensaje privado enviado entre dos usuarios
 * a través de WebSockets.
 * <p>
 * Incluye el usuario destinatario, un asunto y un cuerpo. Este objeto es
 * procesado por el controlador STOMP para enviar mensajes directos.
 */
public class PrivateChatMessage {

    /**
     * Nombre del usuario destinatario del mensaje.
     */
    private String to;

    /**
     * Asunto del mensaje.
     */
    private String subject;

    /**
     * Contenido principal del mensaje.
     */
    private String body;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
