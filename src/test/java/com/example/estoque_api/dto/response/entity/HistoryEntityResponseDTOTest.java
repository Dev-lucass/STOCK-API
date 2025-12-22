package com.example.estoque_api.dto.response.entity;

import com.example.estoque_api.enums.InventoryAction;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class HistoryEntityResponseDTOTest {

    @Test
    void should_create_history_entity_response_dto() {
        var date = LocalDate.now();

        var dto = HistoryEntityResponseDTO.builder()
                .userId(1L)
                .productId(10L)
                .action(InventoryAction.TAKE)
                .quantity(5)
                .createdAt(date)
                .build();

        assertEquals(1L, dto.userId());
        assertEquals(10L, dto.productId());
        assertEquals(InventoryAction.TAKE, dto.action());
        assertEquals(5, dto.quantity());
        assertEquals(date, dto.createdAt());
    }
}
