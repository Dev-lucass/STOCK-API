package com.example.estoque_api.dto.response.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Builder
public record InventoryReturnResponseDTO(Long id,
                                         UUID inventoryId,
                                         Long toolId,
                                         int quantityReturned,
                                         int quantityInitial,
                                         int quantityCurrent,
                                         Double currentLifeCycle,
                                         int usageCount,
                                         @JsonFormat(pattern = "HH:mm:ss")
                                         LocalTime usageTime,
                                         @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                         LocalDateTime returnToolAt) {}
