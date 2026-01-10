package com.example.estoque_api.dto.request.persist;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryDTO(@NotNull @Min(1) int quantity,
                           @NotNull @Min(1) long idTool) {}
