package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.request.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityTakeResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class InventoryEntityMapper {

    public InventoryEntity toEntityInventory(InventoryEntityDTO dto, ToolEntity tool) {
        return InventoryEntity.builder()
                .quantityInitial(dto.quantityInitial())
                .tool(tool)
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

    public InventoryEntityTakeResponseDTO toTakeInventoryResponse(InventoryEntity entity, int quantityTaked) {
        return InventoryEntityTakeResponseDTO.builder()
                .id(entity.getId())
                .inventoryId(entity.getInventoryId())
                .quantityTaked(quantityTaked)
                .quantityCurrent(entity.getQuantityCurrent())
                .quantityInitial(entity.getQuantityInitial())
                .idTool(entity.getTool().getId())
                .createdAt(LocalDate.now())
                .build();
    }

    public InventoryEntityReturnResponseDTO toReturnedInventoryResponse(InventoryEntity entity, int quantityReturned) {
        return InventoryEntityReturnResponseDTO.builder()
                .id(entity.getId())
                .inventoryId(entity.getInventoryId())
                .quantityReturned(quantityReturned)
                .quantityInitial(entity.getQuantityInitial())
                .quantityCurrent(entity.getQuantityCurrent())
                .idTool(entity.getTool().getId())
                .createdAt(LocalDate.now())
                .build();
    }

    public HistoryEntityDTO toHistoryEntityDTO(TakeFromInventory take, UserEntity user, ToolEntity tool, InventoryAction action) {
        return HistoryEntityDTO.builder()
                .quantityTaken(take.quantityTaken())
                .inventoryId(take.inventoryId())
                .user(user)
                .tool(tool)
                .action(action)
                .build();
    }

    public void updateEntity(InventoryEntity entity, InventoryEntityDTO dto, ToolEntity product) {
        entity.setQuantityInitial(dto.quantityInitial());
        entity.setQuantityCurrent(dto.quantityInitial());
        entity.setTool(product);
    }
}
