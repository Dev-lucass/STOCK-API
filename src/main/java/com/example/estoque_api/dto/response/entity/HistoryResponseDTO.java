package com.example.estoque_api.dto.response.entity;

import com.example.estoque_api.enums.InventoryAction;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record HistoryResponseDTO(long historyId,
                                 long inventoryId,
                                 long userId,
                                 long toolId,
                                 String tool,
                                 InventoryAction action,
                                 int quantityTaken,
                                 @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                 LocalDateTime createdAt) {}
