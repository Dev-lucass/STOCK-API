package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryEntityResponseDTOTest {

    private Long id;
    private String inventoryId;
    private int quantityInitial;
    private int quantityCurrent;
    private Long idTool;
    private LocalDate createdAt;

    @BeforeEach
    void setUp() {
        id = 1L;
        inventoryId = "INV-2025-001";
        quantityInitial = 100;
        quantityCurrent = 85;
        idTool = 500L;
        createdAt = LocalDate.of(2025, 12, 24);
    }

    @Test
    @DisplayName("Should successfully instantiate the record using builder and return correct values")
    void shouldCreateInventoryEntityResponseDTOUsingBuilder() {
        InventoryEntityResponseDTO dto = InventoryEntityResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .quantityInitial(quantityInitial)
                .quantityCurrent(quantityCurrent)
                .idTool(idTool)
                .createdAt(createdAt)
                .build();

        assertAll(
                () -> assertEquals(id, dto.id()),
                () -> assertEquals(inventoryId, dto.inventoryId()),
                () -> assertEquals(quantityInitial, dto.quantityInitial()),
                () -> assertEquals(quantityCurrent, dto.quantityCurrent()),
                () -> assertEquals(idTool, dto.idTool()),
                () -> assertEquals(createdAt, dto.createdAt())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        InventoryEntityResponseDTO dto1 = new InventoryEntityResponseDTO(id, inventoryId, quantityInitial, quantityCurrent, idTool, createdAt);
        InventoryEntityResponseDTO dto2 = new InventoryEntityResponseDTO(id, inventoryId, quantityInitial, quantityCurrent, idTool, createdAt);
        InventoryEntityResponseDTO dto3 = new InventoryEntityResponseDTO(2L, "OTHER-ID", 50, 50, 600L, createdAt);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        InventoryEntityResponseDTO dto = new InventoryEntityResponseDTO(id, inventoryId, quantityInitial, quantityCurrent, idTool, createdAt);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("id=" + id)),
                () -> assertTrue(toString.contains("inventoryId=" + inventoryId)),
                () -> assertTrue(toString.contains("quantityInitial=" + quantityInitial)),
                () -> assertTrue(toString.contains("quantityCurrent=" + quantityCurrent)),
                () -> assertTrue(toString.contains("idTool=" + idTool)),
                () -> assertTrue(toString.contains("createdAt=" + createdAt))
        );
    }
}