package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.response.entity.HistoryEntityResponseDTO;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.HistoryId;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class HistoryEntityMapper {

    public HistoryEntity toEntityHistory(HistoryEntityDTO dto) {
        var historyId = new HistoryId();
        historyId.setUserId(dto.user().getId());
        historyId.setProductId(dto.product().getId());
        historyId.setCreatedAt(LocalDate.now());

        return HistoryEntity.builder()
                .id(historyId)
                .user(dto.user())
                .product(dto.product())
                .quantity(dto.quantity())
                .action(dto.action())
                .build();
    }

    public HistoryEntityResponseDTO toResponseEntityHistory(HistoryEntity entity) {
        return HistoryEntityResponseDTO.builder()
                .userId(entity.getUser().getId())
                .quantity(entity.getQuantity())
                .productId(entity.getProduct().getId())
                .action(entity.getAction())
                .createdAt(LocalDate.now())
                .build();
    }
}
