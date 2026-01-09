package com.example.estoque_api.dto.response.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record InventoryTakeResponseDTO(long id,
                                       long inventoryId,
                                       int quantityTaked,
                                       int quantityCurrent,
                                       int quantityInitial,
                                       long toolId,
                                       double currentLifeCycle,
                                       int usageCount,
                                       @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                       LocalDateTime takeToolAt) {
}
