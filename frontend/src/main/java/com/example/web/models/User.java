package com.example.web.models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(name = "password_hash")
    private String passwordHash;

    private String email;

    private String role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}