package com.example.estoque_api.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryEntityDTOTest {

    @Test
    @DisplayName("Should successfully instantiate the record and return correct values")
    void shouldCreateInventoryEntityDTOAndReturnCorrectValues() {
        int expectedQuantity = 100;
        Long expectedIdTool = 500L;

        InventoryEntityDTO dto = new InventoryEntityDTO(expectedQuantity, expectedIdTool);

        assertAll(
                () -> assertEquals(expectedQuantity, dto.quantityInitial()),
                () -> assertEquals(expectedIdTool, dto.idTool())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        InventoryEntityDTO dto1 = new InventoryEntityDTO(10, 1L);
        InventoryEntityDTO dto2 = new InventoryEntityDTO(10, 1L);
        InventoryEntityDTO dto3 = new InventoryEntityDTO(20, 2L);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        InventoryEntityDTO dto = new InventoryEntityDTO(10, 1L);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("quantityInitial=10")),
                () -> assertTrue(toString.contains("idTool=1"))
        );
    }
}