package com.example.estoque_api.repository.custom;

import com.example.estoque_api.dto.request.filter.PeriodRequestDTO;
import com.example.estoque_api.repository.projection.MetricProjection;
import java.util.List;

public interface HistoryRepositoryCustom {
    List<MetricProjection> findTop10WithFilter(PeriodRequestDTO period);
}