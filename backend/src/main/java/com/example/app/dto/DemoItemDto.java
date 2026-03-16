package com.example.app.web.dto;

import java.time.LocalDateTime;

public record DemoItemDto(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}