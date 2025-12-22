package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class InventoryEntityTakeResponseDTOTest {

    @Test
    void should_create_inventory_entity_take_response_dto() {
        var date = LocalDate.now();

        var dto = InventoryEntityTakeResponseDTO.builder()
                .id(1L)
                .quantityTaked(8)
                .productId(10L)
                .createdAt(date)
                .build();

        assertEquals(1L, dto.id());
        assertEquals(8, dto.quantityTaked());
        assertEquals(10L, dto.productId());
        assertEquals(date, dto.createdAt());
    }
}
