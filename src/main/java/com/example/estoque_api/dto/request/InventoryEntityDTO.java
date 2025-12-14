package com.example.estoque_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryEntityDTO(

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity,

        @NotNull(message = "Product id is required")
        @Min(value = 1, message = "Product id must be greater than 0")
        Long productId
) {}
