package com.example.estoque_api.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryDTOTest {

    @Test
    @DisplayName("Should successfully instantiate the record and return correct values")
    void shouldCreateInventoryEntityDTOAndReturnCorrectValues() {
        int expectedQuantity = 100;
        Long expectedIdTool = 500L;

        InventoryDTO dto = new InventoryDTO(expectedQuantity, expectedIdTool);

        assertAll(
                () -> assertEquals(expectedQuantity, dto.quantity()),
                () -> assertEquals(expectedIdTool, dto.idTool())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        InventoryDTO dto1 = new InventoryDTO(10, 1L);
        InventoryDTO dto2 = new InventoryDTO(10, 1L);
        InventoryDTO dto3 = new InventoryDTO(20, 2L);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        InventoryDTO dto = new InventoryDTO(10, 1L);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("quantity=10")),
                () -> assertTrue(toString.contains("idTool=1"))
        );
    }
}