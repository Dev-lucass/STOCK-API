package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.ProductEntityDTO;
import com.example.estoque_api.model.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ProductEntityMapperTest {

    private ProductEntityMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ProductEntityMapper();
    }

    @Test
    void should_map_dto_to_entity() {
        var dto = new ProductEntityDTO(
                "Teclado Mecânico",
                true
        );

        var entity = mapper.toEntityProduct(dto);

        assertEquals("Teclado Mecânico", entity.getName());
        assertTrue(entity.getActive());
        assertNull(entity.getId());
    }

    @Test
    void should_map_entity_to_response_dto() {
        var entity = ProductEntity.builder()
                .id(1L)
                .name("Teclado Mecânico")
                .active(true)
                .build();

        var response = mapper.toResponseEntityProduct(entity);

        assertEquals(1L, response.id());
        assertEquals("Teclado Mecânico", response.name());
        assertTrue(response.active());
        assertEquals(LocalDate.now(), response.createdAt());
    }

    @Test
    void should_update_entity_from_dto() {
        var entity = ProductEntity.builder()
                .name("Produto Antigo")
                .active(false)
                .build();

        var dto = new ProductEntityDTO(
                "Produto Novo",
                true
        );

        mapper.updateEntity(entity, dto);

        assertEquals("Produto Novo", entity.getName());
        assertTrue(entity.getActive());
    }
}
