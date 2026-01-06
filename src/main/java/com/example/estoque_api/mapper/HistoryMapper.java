package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.internal.HistoryDTO;
import com.example.estoque_api.dto.response.entity.HistoryResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class HistoryMapper {

    public HistoryEntity toEntityHistory(HistoryDTO dto) {
        return HistoryEntity.builder()
                .user(dto.user())
                .inventoryId(dto.inventoryId())
                .tool(dto.tool())
                .quantityTaken(dto.quantityTaken())
                .action(dto.action())
                .build();
    }

    public HistoryDTO buildHistoryDto(int quantity, InventoryAction action, ToolEntity tool, UserEntity user, UUID inventoryId, double currentLlfeCycle) {
        return HistoryDTO.builder()
                .quantityTaken(quantity)
                .action(action)
                .tool(tool)
                .user(user)
                .inventoryId(inventoryId)
                .currentLifeCycle(currentLlfeCycle)
                .build();
    }

    public HistoryResponseDTO toResponseEntityHistory(HistoryEntity entity) {
        return HistoryResponseDTO.builder()
                .historyId(entity.getId())
                .inventoryId(entity.getInventoryId())
                .userId(entity.getUser().getId())
                .tool(entity.getTool().getName())
                .quantityTaken(entity.getQuantityTaken())
                .toolId(entity.getTool().getId())
                .action(entity.getAction())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
