package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class InventoryEntityResponseDTOTest {

    @Test
    void should_create_inventory_entity_response_dto() {
        var date = LocalDate.now();

        var dto = InventoryEntityResponseDTO.builder()
                .id(1L)
                .quantity(50)
                .productId(10L)
                .createdAt(date)
                .build();

        assertEquals(1L, dto.id());
        assertEquals(50, dto.quantity());
        assertEquals(10L, dto.productId());
        assertEquals(date, dto.createdAt());
    }
}
