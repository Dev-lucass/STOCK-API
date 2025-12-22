package com.example.estoque_api.dto.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InventoryEntityDTOTest {

    @Test
    void should_create_inventory_entity_dto() {
        var dto = new InventoryEntityDTO(10, 1L);
        assertEquals(10, dto.quantity());
        assertEquals(1L, dto.productId());
    }

    @Test
    void should_allow_any_values_when_not_validated() {
        var dto = new InventoryEntityDTO(-5, 0L);
        assertEquals(-5, dto.quantity());
        assertEquals(0L, dto.productId());
    }
}
