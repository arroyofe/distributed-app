package com.example.dto;

/**
 * DTO que representa un mensaje de chat enviado a través de WebSockets
 * en modo broadcast.
 * <p>
 * Incluye un asunto y un cuerpo, utilizados para construir el mensaje
 * que se enviará a todos los usuarios conectados.
 */
public class ChatMessage {

    /**
     * Asunto del mensaje.
     */
    private String subject;

    /**
     * Contenido principal del mensaje.
     */
    private String body;

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

