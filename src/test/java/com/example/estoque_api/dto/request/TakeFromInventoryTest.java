package com.example.estoque_api.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TakeFromInventoryTest {

    @Test
    @DisplayName("Should successfully instantiate the record and return correct values")
    void shouldCreateTakeFromInventoryAndReturnCorrectValues() {
        Long expectedUserId = 1L;
        String expectedInventoryId = "INV-999";
        int expectedQuantityTaken = 5;

        TakeFromInventory dto = new TakeFromInventory(
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
        TakeFromInventory dto1 = new TakeFromInventory(1L, "A1", 10);
        TakeFromInventory dto2 = new TakeFromInventory(1L, "A1", 10);
        TakeFromInventory dto3 = new TakeFromInventory(2L, "B2", 5);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        TakeFromInventory dto = new TakeFromInventory(1L, "REF-123", 10);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("userId=1")),
                () -> assertTrue(toString.contains("inventoryId=REF-123")),
                () -> assertTrue(toString.contains("quantityTaken=10"))
        );
    }
}