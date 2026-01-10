package com.example.estoque_api.dto.response.filter;

import lombok.Builder;

@Builder
public record InventoryFilterResponseDTO(long id,
                                         int quantityInitial,
                                         int quantityCurrent,
                                         long toolId,
                                         String nameTool){}
