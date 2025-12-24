package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToolEntityResponseDTOTest {

    private Long id;
    private String name;
    private Boolean active;
    private LocalDate createdAt;

    @BeforeEach
    void setUp() {
        id = 1L;
        name = "Smartphone";
        active = true;
        createdAt = LocalDate.now();
    }

    @Test
    @DisplayName("Should successfully instantiate the record using builder and return correct values")
    void shouldCreateToolEntityResponseDTOUsingBuilder() {
        ToolEntityResponseDTO dto = ToolEntityResponseDTO.builder()
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
    void shouldVerifyEquality() {
        ToolEntityResponseDTO dto1 = new ToolEntityResponseDTO(id, name, active, createdAt);
        ToolEntityResponseDTO dto2 = new ToolEntityResponseDTO(id, name, active, createdAt);
        ToolEntityResponseDTO dto3 = new ToolEntityResponseDTO(2L, "Tablet", false, createdAt);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        ToolEntityResponseDTO dto = new ToolEntityResponseDTO(id, name, active, createdAt);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("id=" + id)),
                () -> assertTrue(toString.contains("name=" + name)),
                () -> assertTrue(toString.contains("active=" + active)),
                () -> assertTrue(toString.contains("createdAt=" + createdAt))
        );
    }
}