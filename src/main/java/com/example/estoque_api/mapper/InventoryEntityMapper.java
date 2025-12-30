package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityTakeResponseDTO;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class InventoryEntityMapper {

    public InventoryEntity toEntityInventory(InventoryEntityDTO dto, ToolEntity tool) {
        return InventoryEntity.builder()
                .quantityInitial(dto.quantity())
                .tool(tool)
                .build();
    }

    public InventoryEntityResponseDTO toResponseEntityInventory(InventoryEntity entity) {
        return InventoryEntityResponseDTO.builder()
                .id(entity.getId())
                .inventoryId(entity.getInventoryId())
                .quantityInitial(entity.getQuantityInitial())
                .quantityCurrent(entity.getQuantityCurrent())
                .idTool(entity.getTool().getId())
                .createdAt(LocalDate.now())
                .build();
    }

    public InventoryEntityTakeResponseDTO toTakeInventoryResponse(InventoryEntity entity, int quantityTaked, int usageCount) {
        return InventoryEntityTakeResponseDTO.builder()
                .id(entity.getId())
                .inventoryId(entity.getInventoryId())
                .quantityTaked(quantityTaked)
                .quantityCurrent(entity.getQuantityCurrent())
                .quantityInitial(entity.getQuantityInitial())
                .idTool(entity.getTool().getId())
                .currentLifeCycle(entity.getTool().getCurrentLifeCycle())
                .usageCount(usageCount)
                .quantityCurrent(entity.getQuantityCurrent())
                .createdAt(LocalDate.now())
                .build();
    }

    public InventoryEntityReturnResponseDTO toReturnedInventoryResponse(InventoryEntity entity, int quantityReturned, int usageCount, LocalTime usageTime) {
        return InventoryEntityReturnResponseDTO.builder()
                .id(entity.getId())
                .inventoryId(entity.getInventoryId())
                .quantityReturned(quantityReturned)
                .quantityInitial(entity.getQuantityInitial())
                .currentLifeCycle(entity.getTool().getCurrentLifeCycle())
                .usageCount(usageCount)
                .usageTime(usageTime)
                .quantityCurrent(entity.getQuantityCurrent())
                .idTool(entity.getTool().getId())
                .createdAt(LocalDate.now())
                .build();
    }

    public void updateEntity(int currentQuantity, int quantityInitial, ToolEntity tool, InventoryEntity inventory) {
        inventory.setQuantityInitial(quantityInitial);
        inventory.setQuantityCurrent(currentQuantity);
        inventory.setTool(tool);
    }
}
