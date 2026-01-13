package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.internal.MetricDTO;
import com.example.estoque_api.dto.request.filter.PeriodRequestDTO;
import com.example.estoque_api.repository.projection.MetricProjection;
import org.springframework.stereotype.Component;

@Component
public class MetricMapper {
    public MetricDTO toMetricResponse(MetricProjection mp, PeriodRequestDTO period, int rank) {
        return MetricDTO.builder()
                .top(rank)
                .toolId(mp.toolId())
                .mostUsedTool(mp.toolName())
                .userId(mp.userId())
                .userUsedMost(mp.topUser())
                .usageCount(mp.usageCount())
                .totalTaked(mp.totalTaked())
                .firstUsage(mp.firstUsage())
                .period(period)
                .build();
    }
}
