package com.example.estoque_api.dto.response;

import com.example.estoque_api.enums.InventoryAction;

import java.time.LocalDateTime;

public record HistoryResponseDTO(
        Long userId,
        Long productId,
        InventoryAction action,
        int quantity,
        LocalDateTime createdAt
) {
}
