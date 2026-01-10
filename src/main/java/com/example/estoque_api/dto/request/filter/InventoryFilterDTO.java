package com.example.estoque_api.dto.request.filter;

import lombok.Builder;

@Builder
public record InventoryFilterDTO(Integer quantityInitial,
                                 Integer quantityCurrent,
                                 String toolName,
                                 Long toolId) {}
