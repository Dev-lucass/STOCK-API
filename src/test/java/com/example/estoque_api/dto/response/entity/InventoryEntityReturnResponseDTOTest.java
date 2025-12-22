package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class InventoryEntityReturnResponseDTOTest {

    @Test
    void should_create_inventory_entity_return_response_dto() {
        var date = LocalDate.now();

        var dto = InventoryEntityReturnResponseDTO.builder()
                .id(1L)
                .quantityReturned(15)
                .productId(10L)
                .createdAt(date)
                .build();

        assertEquals(1L, dto.id());
        assertEquals(15, dto.quantityReturned());
        assertEquals(10L, dto.productId());
        assertEquals(date, dto.createdAt());
    }
}
