package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.ProductEntityDTO;
import com.example.estoque_api.dto.response.entity.ProductEntityResponseDTO;
import com.example.estoque_api.model.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductEntityMapperTest {

    private ProductEntityMapper mapper;
    private ProductEntityDTO productDTO;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        mapper = new ProductEntityMapper();
        
        productDTO = new ProductEntityDTO("Keyboard Mechanical", true);
        
        productEntity = ProductEntity.builder()
                .id(1L)
                .name("Mouse Optical")
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Should map ProductEntityDTO to ProductEntity successfully")
    void shouldMapDtoToEntityProduct() {
        ProductEntity result = mapper.toEntityProduct(productDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(productDTO.name(), result.getName()),
                () -> assertEquals(productDTO.active(), result.getActive())
        );
    }

    @Test
    @DisplayName("Should map ProductEntity to ProductEntityResponseDTO successfully")
    void shouldMapEntityToResponseEntityProduct() {
        ProductEntityResponseDTO result = mapper.toResponseEntityProduct(productEntity);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(productEntity.getId(), result.id()),
                () -> assertEquals(productEntity.getName(), result.name()),
                () -> assertEquals(productEntity.getActive(), result.active()),
                () -> assertEquals(LocalDate.now(), result.createdAt())
        );
    }

    @Test
    @DisplayName("Should update existing ProductEntity with DTO data")
    void shouldUpdateEntity() {
        mapper.updateEntity(productEntity, productDTO);

        assertAll(
                () -> assertEquals(productDTO.name(), productEntity.getName()),
                () -> assertEquals(productDTO.active(), productEntity.getActive())
        );
    }
}