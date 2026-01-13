package com.example.estoque_api.dto.internal;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record HistoryDTO(UserEntity user,
                         long inventoryId,
                         ToolEntity tool,
                         InventoryAction action,
                         int quantityTaken,
                         double currentLifeCycle,
                         @JsonFormat(pattern = "HH:mm:ss")
                         LocalDate createdAt){}
