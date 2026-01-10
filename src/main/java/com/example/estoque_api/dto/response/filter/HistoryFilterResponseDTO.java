package com.example.estoque_api.dto.response.filter;

import com.example.estoque_api.enums.InventoryAction;
import lombok.Builder;

@Builder
public record HistoryFilterResponseDTO(long id,
                                       long inventoryId,
                                       InventoryAction action,
                                       UserFilterResponseDTO user,
                                       ToolFilterResponseDTO tool){}
