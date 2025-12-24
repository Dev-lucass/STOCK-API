package com.example.estoque_api.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TakeFromInventoryTest {

    @Test
    @DisplayName("Should successfully instantiate the record and return correct values")
    void shouldCreateTakeFromInventoryAndReturnCorrectValues() {
        Long expectedUserId = 1L;
        String expectedInventoryId = "INV-999";
        int expectedQuantityTaken = 5;
        int expectedTotalQuantityTaken = 50;

        TakeFromInventory dto = new TakeFromInventory(
                expectedUserId,
                expectedInventoryId,
                expectedQuantityTaken,
                expectedTotalQuantityTaken
        );

        assertAll(
                () -> assertEquals(expectedUserId, dto.userId()),
                () -> assertEquals(expectedInventoryId, dto.inventoryId()),
                () -> assertEquals(expectedQuantityTaken, dto.quantityTaken()),
                () -> assertEquals(expectedTotalQuantityTaken, dto.totalQuantityTaken())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        TakeFromInventory dto1 = new TakeFromInventory(1L, "A1", 10, 20);
        TakeFromInventory dto2 = new TakeFromInventory(1L, "A1", 10, 20);
        TakeFromInventory dto3 = new TakeFromInventory(2L, "B2", 5, 15);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        TakeFromInventory dto = new TakeFromInventory(1L, "REF-123", 10, 100);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("userId=1")),
                () -> assertTrue(toString.contains("inventoryId=REF-123")),
                () -> assertTrue(toString.contains("quantityTaken=10")),
                () -> assertTrue(toString.contains("totalQuantityTaken=100"))
        );
    }
}