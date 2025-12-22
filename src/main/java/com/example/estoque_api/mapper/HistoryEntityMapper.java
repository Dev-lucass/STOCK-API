package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.response.entity.HistoryResponseDTO;
import com.example.estoque_api.model.HistoryEntity;
import java.time.LocalDate;

public class HistoryEntityMapper {

    public HistoryEntity toEntityHistory(HistoryEntityDTO dto) {
        return HistoryEntity.builder()
                .quantity(dto.quantity())
                .user(dto.user())
                .product(dto.product())
                .action(dto.action())
                .build();
    }

    public HistoryResponseDTO toResponseEntityHistory(HistoryEntity entity) {
        return HistoryResponseDTO.builder()
                .id(entity.getId())
                .quantity(entity.getQuantity())
                .productId(entity.getProduct().getId())
                .action(entity.getAction())
                .createdAt(LocalDate.now())
                .build();
    }

}
