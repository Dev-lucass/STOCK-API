package com.example.estoque_api.dto.request.filter;

import com.example.estoque_api.enums.InventoryAction;

public record HistoryFilterDTO(Long inventoryId,
                               InventoryAction action,
                               String username,
                               String cpf,
                               Boolean userActive,
                               String toolName,
                               Boolean toolActive,
                               Boolean inUse,
                               Integer usageCount,
                               Integer hourUsage,
                               Integer minutesUsage,
                               Integer secondsUsage){}
