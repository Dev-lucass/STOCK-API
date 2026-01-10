package com.example.estoque_api.dto.request.filter;

public record InventoryFilterDTO(Integer quantityInitial,
                                 Integer quantityCurrent,
                                 String toolName,
                                 Long toolId) {}
