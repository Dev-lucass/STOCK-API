package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.response.InventoryEntityResponseDTO;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ProductEntity;
import java.time.LocalDate;

public class InventoryEntityMapper {

    public InventoryEntity toEntityInventory(InventoryEntityDTO dto, ProductEntity product) {
        return InventoryEntity.builder()
                .quantity(dto.quantity())
                .product(product)
                .build();
    }

    public InventoryEntityResponseDTO toResponseEntityInventory(InventoryEntity entity) {
        return InventoryEntityResponseDTO.builder()
                .id(entity.getId())
                .quantity(entity.getQuantity())
                .productId(entity.getProduct().getId())
                .createdAt(LocalDate.now())
                .build();
    }
}
