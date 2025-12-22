package com.example.estoque_api.dto.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductEntityDTOTest {

    @Test
    void should_create_product_entity_dto() {
        var dto = new ProductEntityDTO("Teclado Mecânico", true);
        assertEquals("Teclado Mecânico", dto.name());
        assertTrue(dto.active());
    }

    @Test
    void should_allow_blank_or_null_values_when_not_validated() {
        var dto = new ProductEntityDTO("", null);
        assertEquals("", dto.name());
        assertNull(dto.active());
    }
}
