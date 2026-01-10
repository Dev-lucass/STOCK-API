package com.example.estoque_api.dto.request.filter;

import lombok.Builder;

@Builder
public record ToolFilterDTO(String toolName,
                            Boolean toolActive,
                            Boolean inUse,
                            Integer usageCount,
                            Integer hourUsage,
                            Integer minutesUsage,
                            Integer secondsUsage){}
