package com.example.estoque_api.dto.internal;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import lombok.Builder;
import java.util.UUID;

@Builder
public record HistoryEntityDTO(UserEntity user,
                               UUID inventoryId,
                               ToolEntity tool,
                               InventoryAction action,
                               int quantityTaken) {}
