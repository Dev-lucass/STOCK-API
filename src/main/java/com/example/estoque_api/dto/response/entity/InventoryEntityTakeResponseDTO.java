package com.example.estoque_api.dto.response.entity;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.UUID;

@Builder
public record InventoryEntityTakeResponseDTO(Long id,
                                             UUID inventoryId,
                                             int quantityTaked,
                                             int quantityCurrent,
                                             int quantityInitial,
                                             Long idTool,
                                             Double currentLifeCycle,
                                             int usageCount,
                                             @DateTimeFormat(pattern = "dd/MM/yyyy")
                                             LocalDate createdAt) {
}
