package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.ProductEntityDTO;
import com.example.estoque_api.dto.response.ProductEntityResponseDTO;
import com.example.estoque_api.model.ProductEntity;
import java.time.LocalDate;

public class ProductEntityMapper {

    public ProductEntity toEntityProduct(ProductEntityDTO dto) {
        return ProductEntity.builder()
                .name(dto.name())
                .active(dto.active())
                .build();
    }

    public ProductEntityResponseDTO toResponseEntityProduct(ProductEntity entity) {
        return ProductEntityResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .active(entity.getActive())
                .createdAt(LocalDate.now())
                .build();
    }

    public void updateEntity(ProductEntity entity, ProductEntityDTO dto) {
        entity.setName(dto.name());
    }
}
