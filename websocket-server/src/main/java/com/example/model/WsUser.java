package com.example.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Entidad que representa un usuario registrado en el sistema WebSocket.
 * <p>
 * Los usuarios se almacenan en la tabla {@code ws_user} y contienen
 * información básica necesaria para autenticación y control de acceso.
 * <p>
 * La contraseña se almacena en formato codificado mediante un
 * {@link org.springframework.security.crypto.password.PasswordEncoder}.
 */
@Entity
@Table(name = "ws_user")
public class WsUser {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario único dentro del sistema.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String username;

    /**
     * Contraseña codificada del usuario.
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * Indica si el usuario está habilitado para autenticarse.
     */
    @Column(nullable = false)
    private boolean enabled = true;

    /**
     * Fecha y hora en que el usuario fue creado.
     *
     * <p>Se asigna automáticamente antes de persistir.</p>
     */
    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    /**
     * Constructor vacío requerido por JPA.
     */
    public WsUser() {}

    /**
     * Establece la fecha de creación antes de insertar el registro.
     */
    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    // Getters y setters
    public Long getId() {
        return id; }

    public void setId(Long id) {
        this.id = id; }

    public String getUsername() {
        return username; }

    public void setUsername(String username) {
        this.username = username; }

    public String getPassword() {
        return password; }

    public void setPassword(String password) {
        this.password = password; }

    public boolean isEnabled() {
        return enabled; }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled; }

    public Instant getCreatedAt() {
        return createdAt; }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt; }


}