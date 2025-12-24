package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryEntityReturnResponseDTOTest {

    private Long id;
    private String inventoryId;
    private int quantityReturned;
    private int quantityInitial;
    private int quantityCurrent;
    private Long productId;
    private LocalDate createdAt;

    @BeforeEach
    void setUp() {
        id = 1L;
        inventoryId = "INV-RETURN-001";
        quantityReturned = 5;
        quantityInitial = 100;
        quantityCurrent = 95;
        productId = 500L;
        createdAt = LocalDate.now();
    }

    @Test
    @DisplayName("Should successfully instantiate the record using builder and return correct values")
    void shouldCreateInventoryEntityReturnResponseDTOUsingBuilder() {
        InventoryEntityReturnResponseDTO dto = InventoryEntityReturnResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .quantityReturned(quantityReturned)
                .quantityInitial(quantityInitial)
                .quantityCurrent(quantityCurrent)
                .productId(productId)
                .createdAt(createdAt)
                .build();

        assertAll(
                () -> assertEquals(id, dto.id()),
                () -> assertEquals(inventoryId, dto.inventoryId()),
                () -> assertEquals(quantityReturned, dto.quantityReturned()),
                () -> assertEquals(quantityInitial, dto.quantityInitial()),
                () -> assertEquals(quantityCurrent, dto.quantityCurrent()),
                () -> assertEquals(productId, dto.productId()),
                () -> assertEquals(createdAt, dto.createdAt())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        InventoryEntityReturnResponseDTO dto1 = new InventoryEntityReturnResponseDTO(id, inventoryId, quantityReturned, quantityInitial, quantityCurrent, productId, createdAt);
        InventoryEntityReturnResponseDTO dto2 = new InventoryEntityReturnResponseDTO(id, inventoryId, quantityReturned, quantityInitial, quantityCurrent, productId, createdAt);
        InventoryEntityReturnResponseDTO dto3 = new InventoryEntityReturnResponseDTO(2L, "DIFF-ID", 10, 50, 40, 600L, createdAt);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        InventoryEntityReturnResponseDTO dto = new InventoryEntityReturnResponseDTO(id, inventoryId, quantityReturned, quantityInitial, quantityCurrent, productId, createdAt);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("id=" + id)),
                () -> assertTrue(toString.contains("inventoryId=" + inventoryId)),
                () -> assertTrue(toString.contains("quantityReturned=" + quantityReturned)),
                () -> assertTrue(toString.contains("quantityCurrent=" + quantityCurrent)),
                () -> assertTrue(toString.contains("productId=" + productId))
        );
    }
}