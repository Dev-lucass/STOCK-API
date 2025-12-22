package com.example.estoque_api.dto.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TakeFromInventoryTest {

    @Test
    void should_create_take_from_inventory_dto() {
        var dto = new TakeFromInventory(1L, 5, 10L);

        assertEquals(1L, dto.userId());
        assertEquals(5, dto.quantity());
        assertEquals(10L, dto.productId());
    }

    @Test
    void should_allow_any_values_when_not_validated() {
        var dto = new TakeFromInventory(null, -10, null);

        assertNull(dto.userId());
        assertEquals(-10, dto.quantity());
        assertNull(dto.productId());
    }
}
