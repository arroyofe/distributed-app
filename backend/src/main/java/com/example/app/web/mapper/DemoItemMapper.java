package com.example.app.web.mapper;

import com.example.app.domain.DemoItem;
import com.example.app.dto.DemoItemDto;
import com.example.app.dto.DemoItemCreateUpdateDto;

public class DemoItemMapper {
    public static DemoItemDto toDto(DemoItem e) {
        return new DemoItemDto(e.getId(), e.getName(), e.getDescription(), e.getCreatedAt(), e.getUpdatedAt());
    }
    public static void apply(DemoItem e, DemoItemCreateUpdateDto dto) {
        e.setName(dto.name());
        e.setDescription(dto.description());
    }
}