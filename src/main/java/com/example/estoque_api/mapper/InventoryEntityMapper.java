package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityTakeResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
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

    public InventoryEntityTakeResponseDTO toTakeInventoryResponse(InventoryEntity entity, int quantityTaked) {
        return InventoryEntityTakeResponseDTO.builder()
                .id(entity.getId())
                .quantityTaked(quantityTaked)
                .productId(entity.getProduct().getId())
                .createdAt(LocalDate.now())
                .build();
    }

    public List<InventoryEntityReturnResponseDTO> toReturnedInventoryResponse(InventoryEntity entity, int quantityReturned) {
        return List.of(InventoryEntityReturnResponseDTO.builder()
                .id(entity.getId())
                .quantityReturned(quantityReturned)
                .productId(entity.getProduct().getId())
                .createdAt(LocalDate.now())
                .build()
        );
    }

    public HistoryEntityDTO toHistoryEntityDTO(int quantity, UserEntity user, ProductEntity product, InventoryAction action) {
        return HistoryEntityDTO.builder()
                .quantity(quantity)
                .user(user)
                .product(product)
                .action(action)
                .build();
    }

    public void updateEntity(InventoryEntity entity, InventoryEntityDTO dto, ProductEntity product) {
        entity.setQuantity(dto.quantity());
        entity.setProduct(product);
    }
}
