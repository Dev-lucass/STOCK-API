package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.InventoryDTO;
import com.example.estoque_api.dto.response.entity.InventoryResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryTakeResponseDTO;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class InventoryMapper {

    public InventoryEntity toEntityInventory(InventoryDTO dto, ToolEntity tool) {
        return InventoryEntity.builder()
                .quantityInitial(dto.quantity())
                .tool(tool)
                .build();
    }

    public InventoryResponseDTO toResponseEntityInventory(InventoryEntity entity) {
        return InventoryResponseDTO.builder()
                .id(entity.getId())
                .inventoryId(entity.getInventoryId())
                .quantityInitial(entity.getQuantityInitial())
                .quantityCurrent(entity.getQuantityCurrent())
                .toolId(entity.getTool().getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public InventoryTakeResponseDTO toTakeInventoryResponse(InventoryEntity entity, int quantityTaked, int usageCount) {
        return InventoryTakeResponseDTO.builder()
                .id(entity.getId())
                .inventoryId(entity.getInventoryId())
                .quantityTaked(quantityTaked)
                .quantityCurrent(entity.getQuantityCurrent())
                .quantityInitial(entity.getQuantityInitial())
                .toolId(entity.getTool().getId())
                .currentLifeCycle(entity.getTool().getCurrentLifeCycle())
                .usageCount(usageCount)
                .quantityCurrent(entity.getQuantityCurrent())
                .takeToolAt(LocalDateTime.now())
                .build();
    }

    public InventoryReturnResponseDTO toReturnedInventoryResponse(InventoryEntity entity, int quantityReturned, int usageCount, LocalTime usageTime) {
        return InventoryReturnResponseDTO.builder()
                .id(entity.getId())
                .inventoryId(entity.getInventoryId())
                .quantityReturned(quantityReturned)
                .quantityInitial(entity.getQuantityInitial())
                .currentLifeCycle(entity.getTool().getCurrentLifeCycle())
                .usageCount(usageCount)
                .usageTime(usageTime)
                .quantityCurrent(entity.getQuantityCurrent())
                .toolId(entity.getTool().getId())
                .returnToolAt(LocalDateTime.now())
                .build();
    }

    public void updateEntity(int currentQuantity, int quantityInitial, ToolEntity tool, InventoryEntity inventory) {
        inventory.setQuantityInitial(quantityInitial);
        inventory.setQuantityCurrent(currentQuantity);
        inventory.setTool(tool);
    }
}
