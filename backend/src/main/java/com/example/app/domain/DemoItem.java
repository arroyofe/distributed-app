package com.example.app.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}