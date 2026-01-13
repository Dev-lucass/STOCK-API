package com.example.estoque_api.repository.projection;

import java.time.LocalDateTime;

public record MetricProjection(
        Long toolId,
        String toolName,
        LocalDateTime firstUsage,
        Integer usageCount,
        Integer totalTaked,
        Long userId,
        String topUser){}