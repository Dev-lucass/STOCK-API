package com.example.estoque_api.dto.response;

import java.time.LocalDateTime;

public record InventoryEntityResponseDTO(
        Integer quantity,
        Long productId,
        LocalDateTime createdAt
) {
}
