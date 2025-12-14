package com.example.estoque_api.dto.response.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record InventoryTakeResponseDTO(Long id,
                                       UUID inventoryId,
                                       int quantityTaked,
                                       int quantityCurrent,
                                       int quantityInitial,
                                       Long toolId,
                                       Double currentLifeCycle,
                                       int usageCount,
                                       @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                       LocalDateTime takeToolAt) {
}
