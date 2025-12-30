package com.example.estoque_api.dto.response.entity;

import com.example.estoque_api.enums.InventoryAction;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.UUID;

@Builder
public record HistoryEntityResponseDTO(Long historyId,
                                       UUID inventoryId,
                                       Long userId,
                                       Long idTool,
                                       InventoryAction action,
                                       int quantityTaken,
                                       @DateTimeFormat(pattern = "dd/MM/yyyy")
                                       LocalDate createdAt) {}
