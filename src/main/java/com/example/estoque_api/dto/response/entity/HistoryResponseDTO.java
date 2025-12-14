package com.example.estoque_api.dto.response.entity;

import com.example.estoque_api.enums.InventoryAction;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record HistoryResponseDTO(Long historyId,
                                 UUID inventoryId,
                                 Long userId,
                                 Long toolId,
                                 String tool,
                                 InventoryAction action,
                                 int quantityTaken,
                                 @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                 LocalDateTime createdAt) {}
