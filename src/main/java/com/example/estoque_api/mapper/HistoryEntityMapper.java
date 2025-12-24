package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.response.entity.HistoryEntityResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class HistoryEntityMapper {

    public HistoryEntity toEntityHistory(HistoryEntityDTO dto) {
        return HistoryEntity.builder()
                .user(dto.user())
                .inventoryId(dto.inventoryId())
                .tool(dto.tool())
                .quantityTaken(dto.quantityTaken())
                .action(dto.action())
                .build();
    }

    public HistoryEntityDTO buildHistoryDto(int quantity, InventoryAction action, ToolEntity tool, UserEntity user, String inventoryId) {
        return HistoryEntityDTO.builder()
                .quantityTaken(quantity)
                .action(action)
                .tool(tool)
                .user(user)
                .inventoryId(inventoryId)
                .build();
    }

    public HistoryEntityResponseDTO toResponseEntityHistory(HistoryEntity entity) {
        return HistoryEntityResponseDTO.builder()
                .historyId(entity.getId())
                .inventoryId(entity.getInventoryId())
                .userId(entity.getUser().getId())
                .quantityTaken(entity.getQuantityTaken())
                .idTool(entity.getTool().getId())
                .action(entity.getAction())
                .createdAt(LocalDate.now())
                .build();
    }
}
