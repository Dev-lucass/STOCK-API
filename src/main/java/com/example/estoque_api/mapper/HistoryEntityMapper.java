package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.response.entity.HistoryEntityResponseDTO;
import com.example.estoque_api.model.HistoryEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class HistoryEntityMapper {

    public HistoryEntity toEntityHistory(HistoryEntityDTO dto) {
        return HistoryEntity.builder()
                .user(dto.user())
                .inventoryId(dto.inventoryId())
                .product(dto.product())
                .quantityTaken(dto.quantityTaken())
                .action(dto.action())
                .build();
    }

    public HistoryEntityResponseDTO toResponseEntityHistory(HistoryEntity entity) {
        return HistoryEntityResponseDTO.builder()
                .historyId(entity.getId())
                .inventoryId(entity.getInventoryId())
                .userId(entity.getUser().getId())
                .quantityTaken(entity.getQuantityTaken())
                .productId(entity.getProduct().getId())
                .action(entity.getAction())
                .createdAt(LocalDate.now())
                .build();
    }
}
