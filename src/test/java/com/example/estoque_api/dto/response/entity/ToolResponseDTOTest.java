package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToolResponseDTOTest {

    private Long id;
    private String name;
    private boolean active;
    private LocalDateTime createdAt;

    @BeforeEach
    void setUp() {
        id = 1L;
        name = "Smartphone";
        active = true;
        createdAt = LocalDateTime.now();
    }

    @Test
    @DisplayName("Should successfully instantiate the record using builder and return correct values")
    void shouldCreateToolEntityResponseDTOUsingBuilder() {
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
    @DisplayName("Should verify equality between two instances with same values")
    void should_Verify_Equality() {
        var dto1 = new ToolResponseDTO(id, name, active, createdAt);
        var dto2 = new ToolResponseDTO(id, name, active, createdAt);
        var dto3 = new ToolResponseDTO(2L, "Tablet", false, createdAt);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void should_Verify_ToString() {
        var dto = new ToolResponseDTO(id, name, active, createdAt);
        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("id=" + id)),
                () -> assertTrue(toString.contains("toolName=" + name)),
                () -> assertTrue(toString.contains("userActive=" + active)),
                () -> assertTrue(toString.contains("usageTime=" + createdAt))
        );
    }
}