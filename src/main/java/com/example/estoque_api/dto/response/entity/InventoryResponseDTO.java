package com.example.estoque_api.dto.response.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record InventoryResponseDTO(Long id,
                                   UUID inventoryId,
                                   int quantityInitial,
                                   int quantityCurrent,
                                   Long toolId,
                                   @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                   LocalDateTime createdAt) {}


