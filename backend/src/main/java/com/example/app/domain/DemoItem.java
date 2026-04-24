package com.example.app.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Tabla de demostración, Elemento almacenado en una base de datos. Esta entidad
 * muestr una estructura simple con gestión de fechas de creación y actualización.
 */
@Entity
@Table(name = "demo_item",
        indexes = {
                @Index(name = "ix_demo_item_created_at", columnList = "created_at")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_demo_item_name", columnNames = "name")
        })
@Getter @Setter
public class DemoItem {
    /**
     * Identificador único del elemento, autoincrementado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Descripción del elemento
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Descripcin del elemento
     */
    @Column(length = 255)
    private String description;

    /**
     * Fecha y hora de creaciän del elemento
     * No se puede modificar tras la inserción.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de la última actualizadión?
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}