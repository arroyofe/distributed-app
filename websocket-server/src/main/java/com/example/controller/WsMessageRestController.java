package com.example.controller;

import com.example.model.WsMessage;
import org.springframework.web.bind.annotation.*;
import com.example.services.WsMessageService;

import java.security.Principal;
import java.util.List;

/**
 * Controlador REST encargado de gestionar la recuperación y actualización
 * del estado de los mensajes WebSocket almacenados en el sistema.
 * <p>
 * Proporciona endpoints para obtener mensajes pendientes de entrega
 * y para marcar mensajes como entregados una vez que el cliente los recibe.
 * <p>
 * Este controlador actúa como puente entre el cliente HTTP y el servicio
 * de persistencia de mensajes.
 */
@RestController
@RequestMapping("/ws-messages")
public class WsMessageRestController {

    /**
     * Servicio encargado de gestionar la lógica de almacenamiento y actualización
     * del estado de los mensajes WebSocket.
     */
    private final WsMessageService messageService;

    /**
     * Crea una nueva instancia del controlador de mensajes WebSocket.
     *
     * @param messageService servicio de gestión de mensajes.
     */
    public WsMessageRestController(WsMessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Obtiene los mensajes pendientes de entrega para el usuario autenticado.
     * <p>
     * El usuario se determina a partir del {@link Principal} asociado a la
     * sesión HTTP. Solo se devuelven mensajes cuyo estado indica que aún no
     * han sido entregados.
     *
     * @param principal información del usuario autenticado.
     * @return lista de mensajes pendientes.
     */
    @GetMapping("/pending")
    public List<WsMessage> getPendingMessages(Principal principal) {
        return messageService.getPendingMessages(principal.getName());
    }

    /**
     * Marca como entregados los mensajes cuyos IDs se reciben en la petición.
     * <p>
     * ste endpoint es utilizado por el cliente para confirmar que los
     * mensajes han sido recibidos correctamente, permitiendo actualizar su
     * estado en la base de datos.
     *
     * @param ids lista de identificadores de mensajes a marcar como entregados.
     */
    @PostMapping("/mark-delivered")
    public void markDelivered(@RequestBody List<Long> ids) {
        List<WsMessage> messages = ids.stream()
                .map(id -> (WsMessage) messageService.getMessageById(id))
                .toList();

        messageService.markAsDelivered(messages);
    }
}
