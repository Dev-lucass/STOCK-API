package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.MetricDTO;
import com.example.estoque_api.dto.request.filter.PeriodRequestDTO;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.MetricMapper;
import com.example.estoque_api.repository.custom.HistoryRepositoryCustomImpl;
import com.example.estoque_api.repository.projection.MetricProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MetricService {

    private final HistoryRepositoryCustomImpl repositoryCustom;
    private final MetricMapper mapper;

    public List<MetricDTO> findTop10ForMetrics(PeriodRequestDTO period) {
        var filter = repositoryCustom.findTop10WithFilter(period);
        validateQuantityTools(filter);
        return generateTopRank(filter, period);
    }

    private List<MetricDTO> generateTopRank(List<MetricProjection> projectionList, PeriodRequestDTO period) {
        return IntStream.range(0, projectionList.size())
                .mapToObj(i -> mapper.toMetricResponse(
                        projectionList.get(i),
                        period,
                        i + 1
                )).toList();
    }

    private void validateQuantityTools(List<MetricProjection> top) {
        if (top.size() < 10)
            throw new ResourceNotFoundException("To find the top 10, you need at least 10 registered and used tools");
    }
}