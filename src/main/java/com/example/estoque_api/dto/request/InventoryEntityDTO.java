package com.example.estoque_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryEntityDTO(@NotNull @Min(0) int quantityInitial,
                                                             @NotNull @Min(1) Long idTool) {}
