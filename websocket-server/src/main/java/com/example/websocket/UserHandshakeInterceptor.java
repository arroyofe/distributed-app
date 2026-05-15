package com.example.websocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 * Interceptor para el canal de mensajes STOMP encargado de asociar un usuario
 * autenticado al mensaje durante el proceso de conexión (handshake).
 * <p>
 * Este interceptor se ejecuta antes de que un mensaje sea enviado al canal
 * de entrada y permite extraer el nombre de usuario desde los encabezados
 * nativos enviados por el cliente (por ejemplo, desde JavaScript).
 * <p>
 * Si el comando STOMP recibido es {@code CONNECT}, el interceptor intenta
 * obtener el encabezado {@code username} y, si está presente, lo asigna como
 * el usuario asociado a la sesión WebSocket.
 * <p>
 * Esto permite habilitar funcionalidades como mensajería privada mediante
 * {@code /user/{username}/...}.
 */
@Component
public class UserHandshakeInterceptor implements ChannelInterceptor {

    /**
     * Intercepta el mensaje antes de ser enviado al canal.
     * <p>
     * <Si el mensaje corresponde a un comando STOMP {@code CONNECT}, se
     * extrae el encabezado nativo {@code username}. Si existe, se asigna como
     * el usuario autenticado de la sesión WebSocket.</p>
     *
     * Es clave para permitir que Spring asocie correctamente
     * mensajes privados a usuarios específicos.
     *
     * @param message mensaje entrante del cliente.
     * @param channel canal por el cual se envía el mensaje.
     * @return el mensaje original, con el usuario asignado si corresponde.
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String username = accessor.getFirstNativeHeader("username");

            if (username != null) {
                accessor.setUser(() -> username);
            }
        }
        return message;
    }
}
