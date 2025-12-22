package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ProductEntityResponseDTOTest {

    @Test
    void should_create_product_entity_response_dto() {
        var date = LocalDate.now();

        var dto = ProductEntityResponseDTO.builder()
                .id(1L)
                .name("Teclado Mecânico")
                .active(true)
                .createdAt(date)
                .build();

        assertEquals(1L, dto.id());
        assertEquals("Teclado Mecânico", dto.name());
        assertTrue(dto.active());
        assertEquals(date, dto.createdAt());
    }
}
