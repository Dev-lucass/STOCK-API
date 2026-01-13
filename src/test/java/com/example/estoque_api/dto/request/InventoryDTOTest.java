package com.example.estoque_api.dto.request;

import com.example.estoque_api.dto.request.persist.InventoryDTO;
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
        var expectedQuantity = 100;
        var expectedIdTool = 500L;

        var dto = new InventoryDTO(expectedQuantity, expectedIdTool);

        assertAll(
                () -> assertEquals(expectedQuantity, dto.quantity()),
                () -> assertEquals(expectedIdTool, dto.toolId())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        var dto1 = new InventoryDTO(10, 1L);
        var dto2 = new InventoryDTO(10, 1L);
        var dto3 = new InventoryDTO(20, 2L);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        var dto = new InventoryDTO(10, 1L);
        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("quantity=10")),
                () -> assertTrue(toString.contains("toolId=1"))
        );
    }
}