package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryEntityTakeResponseDTOTest {

    private Long id;
    private String inventoryId;
    private int quantityTaked;
    private int quantityCurrent;
    private int quantityInitial;
    private Long idTool;
    private LocalDate createdAt;

    @BeforeEach
    void setUp() {
        id = 1L;
        inventoryId = "INV-TAKE-001";
        quantityTaked = 20;
        quantityCurrent = 80;
        quantityInitial = 100;
        idTool = 500L;
        createdAt = LocalDate.now();
    }

    @Test
    @DisplayName("Should successfully instantiate the record using builder and return correct values")
    void shouldCreateInventoryEntityTakeResponseDTOUsingBuilder() {
        InventoryEntityTakeResponseDTO dto = InventoryEntityTakeResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .quantityTaked(quantityTaked)
                .quantityCurrent(quantityCurrent)
                .quantityInitial(quantityInitial)
                .idTool(idTool)
                .createdAt(createdAt)
                .build();

        assertAll(
                () -> assertEquals(id, dto.id()),
                () -> assertEquals(inventoryId, dto.inventoryId()),
                () -> assertEquals(quantityTaked, dto.quantityTaked()),
                () -> assertEquals(quantityCurrent, dto.quantityCurrent()),
                () -> assertEquals(quantityInitial, dto.quantityInitial()),
                () -> assertEquals(idTool, dto.idTool()),
                () -> assertEquals(createdAt, dto.createdAt())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        InventoryEntityTakeResponseDTO dto1 = new InventoryEntityTakeResponseDTO(id, inventoryId, quantityTaked, quantityCurrent, quantityInitial, idTool, createdAt);
        InventoryEntityTakeResponseDTO dto2 = new InventoryEntityTakeResponseDTO(id, inventoryId, quantityTaked, quantityCurrent, quantityInitial, idTool, createdAt);
        InventoryEntityTakeResponseDTO dto3 = new InventoryEntityTakeResponseDTO(2L, "OTHER-ID", 5, 45, 50, 600L, createdAt);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        InventoryEntityTakeResponseDTO dto = new InventoryEntityTakeResponseDTO(id, inventoryId, quantityTaked, quantityCurrent, quantityInitial, idTool, createdAt);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("id=" + id)),
                () -> assertTrue(toString.contains("inventoryId=" + inventoryId)),
                () -> assertTrue(toString.contains("quantityTaked=" + quantityTaked)),
                () -> assertTrue(toString.contains("quantityCurrent=" + quantityCurrent)),
                () -> assertTrue(toString.contains("idTool=" + idTool))
        );
    }
}