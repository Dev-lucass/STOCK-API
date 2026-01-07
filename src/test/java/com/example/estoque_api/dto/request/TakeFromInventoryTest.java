package com.example.estoque_api.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class TakeFromInventoryTest {

    @Test
    @DisplayName("Should successfully instantiate the record and return correct values")
    void shouldCreateTakeFromInventoryAndReturnCorrectValues() {
        var expectedUserId = 1L;
        var expectedInventoryId = UUID.randomUUID();
        var expectedQuantityTaken = 5;

        var dto = new TakeFromInventory(
                expectedUserId,
                expectedInventoryId,
                expectedQuantityTaken
        );

        assertAll(
                () -> assertEquals(expectedUserId, dto.userId()),
                () -> assertEquals(expectedInventoryId, dto.inventoryId()),
                () -> assertEquals(expectedQuantityTaken, dto.quantityTaken())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        var inventoryId = UUID.randomUUID();
        var dto1 = new TakeFromInventory(1L, inventoryId, 10);
        var dto2 = new TakeFromInventory(1L, inventoryId, 10);
        var dto3 = new TakeFromInventory(2L, inventoryId, 5);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        var inventoryId = UUID.randomUUID();
        var dto = new TakeFromInventory(1L, inventoryId, 10);
        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("userId=1")),
                () -> assertTrue(toString.contains("inventoryId="+ inventoryId)),
                () -> assertTrue(toString.contains("quantityTaken=10"))
        );
    }
}