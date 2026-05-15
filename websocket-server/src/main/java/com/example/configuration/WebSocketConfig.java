package com.example.configuration;

import java.security.Principal;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import com.example.websocket.UserHandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.Map;

/**
 * Configuración principal para el sistema de WebSockets usando STOMP en la aplicación.
 * <p>
 * Esta clase habilita el broker de mensajes, registra los endpoints WebSocket,
 * configura el canal de entrada para interceptar conexiones y define el manejo
 * personalizado del usuario durante el handshake.
 * <p>
 * Incluye soporte para programación con {@code @EnableScheduling} y permite
 * la comunicación en tiempo real entre clientes y servidor.
 *
 */
@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Interceptor encargado de extraer y asociar información del usuario
     * durante el proceso de handshake WebSocket.
     */
    private final UserHandshakeInterceptor userHandshakeInterceptor;

    /**
     * Crea una nueva instancia de configuración WebSocket.
     *
     * @param interceptor interceptor personalizado para manejar información del usuario
     *                    durante el handshake.
     */
    public WebSocketConfig(UserHandshakeInterceptor interceptor) {
        this.userHandshakeInterceptor = interceptor;
    }

    /**
     * Registra el endpoint STOMP accesible para los clientes WebSocket.
     * <p>
     * Define el endpoint {@code /ws}, permite orígenes dinámicos mediante
     * {@code setAllowedOriginPatterns("*")} y establece un {@link DefaultHandshakeHandler}
     * personalizado para determinar el usuario conectado a partir de parámetros
     * en la URL (por ejemplo: {@code ?user=nombre}).
     *
     * @param registry registro de endpoints STOMP.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request,
                                                      WebSocketHandler wsHandler,
                                                      Map<String, Object> attributes) {

                        // Extrae el usuario desde la query (?user=...)
                        String query = request.getURI().getQuery();
                        if (query != null && query.contains("user=")) {
                            String user = java.util.Arrays.stream(query.split("&"))
                                    .filter(s -> s.startsWith("user="))
                                    .map(s -> s.substring(5))
                                    .findFirst()
                                    .orElse(null);
                            return () -> user;
                        }
                        return null;
                    }
                });
    }

    /**
     * Configura el broker de mensajes para la comunicación entre cliente y servidor.
     * <p>
     * Incluye:
     * <p>
     *  Broker simple para destinos {@code /topic} y {@code /queue}.
     *  Prefijo {@code /app} para mensajes enviados desde el cliente al servidor.
     *  Prefijo {@code /user} para mensajes privados dirigidos a usuarios específicos.
     * <
     *
     * @param registry registro del broker de mensajes.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * Registra interceptores para el canal de entrada de mensajes STOMP.
     * <p>
     * Permite interceptar y validar mensajes entrantes, así como asociar
     * información del usuario antes de que sean procesados.</p>
     *
     * @param registration registro del canal de entrada.
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(userHandshakeInterceptor);
    }

    /**
     * Método ejecutado después de la construcción del bean.
     * <p>
     * Imprime en consola la ubicación desde donde se cargó la clase,
     * útil para depuración en entornos con múltiples classloaders.</p>
     */
    @PostConstruct
    public void debugLoadedConfig() {
        System.out.println(">>> WebSocketConfig cargada desde: "
                + this.getClass().getProtectionDomain()
                .getCodeSource()
                .getLocation());
    }
}
