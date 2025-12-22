package com.example.estoque_api.dto.response.entity;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryId;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Builder
public record HistoryResponseDTO(HistoryId id,
                                                                 Long userId,
                                                                 Long productId,
                                                                 InventoryAction action,
                                                                 int quantity,
                                                                @DateTimeFormat(pattern = "dd/MM/yyyy")
                                                                 LocalDate createdAt) {}
