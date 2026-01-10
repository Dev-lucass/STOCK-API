package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ToolResponseDTOTest {

    private long id;
    private String name;
    private boolean active;
    private LocalDateTime createdAt;

    @BeforeEach
    void setUp() {
        id = 1L;
        name = "Hammer";
        active = true;
        createdAt = LocalDateTime.now();
    }

    @Test
    void shouldCreateToolResponseDTOUsingBuilder() {
        var dto = ToolResponseDTO.builder()
                .id(id)
                .name(name)
                .active(active)
                .createdAt(createdAt)
                .build();

        assertAll(
                () -> assertEquals(id, dto.id()),
                () -> assertEquals(name, dto.name()),
                () -> assertEquals(active, dto.active()),
                () -> assertEquals(createdAt, dto.createdAt())
        );
    }

    @Test
    void shouldVerifyEquality() {
        var dto1 = new ToolResponseDTO(id, name, active, createdAt);
        var dto2 = new ToolResponseDTO(id, name, active, createdAt);
        var dto3 = new ToolResponseDTO(2L, "Screwdriver", false, createdAt);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    void shouldVerifyToString() {
        var dto = new ToolResponseDTO(id, name, active, createdAt);
        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("id=" + id)),
                () -> assertTrue(toString.contains("name=" + name)),
                () -> assertTrue(toString.contains("active=" + active)),
                () -> assertTrue(toString.contains("createdAt=" + createdAt))
        );
    }
}