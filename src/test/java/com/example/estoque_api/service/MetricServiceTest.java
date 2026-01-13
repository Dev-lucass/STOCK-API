package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.MetricDTO;
import com.example.estoque_api.dto.request.filter.PeriodRequestDTO;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.MetricMapper;
import com.example.estoque_api.repository.custom.HistoryRepositoryCustomImpl;
import com.example.estoque_api.repository.projection.MetricProjection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricServiceTest {

    @Mock
    private HistoryRepositoryCustomImpl repositoryCustom;

    @Mock
    private MetricMapper mapper;

    @InjectMocks
    private MetricService metricService;

    @Test
    void should_return_list_of_metrics_when_ten_or_more_tools_are_found() {
        var period = new PeriodRequestDTO(LocalDate.now(), LocalDate.now());

        var projections = IntStream.range(0, 10)
                .mapToObj(i -> mock(MetricProjection.class))
                .collect(Collectors.toList());

        when(repositoryCustom.findTop10WithFilter(period))
                .thenReturn(projections);

        when(mapper.toMetricResponse(any(MetricProjection.class), eq(period), anyInt()))
                .thenReturn(MetricDTO.builder()
                        .top(1)
                        .usageCount(20)
                        .totalTaked(200).build());

        var result = metricService.findTop10ForMetrics(period);

        assertThat(result).hasSize(10);
        verify(repositoryCustom, times(1)).findTop10WithFilter(period);
        verify(mapper, times(10)).toMetricResponse(any(), eq(period), anyInt());
    }

    @Test
    void should_throw_exception_when_less_than_ten_tools_are_found() {
        var period = new PeriodRequestDTO(LocalDate.now(), LocalDate.now());
        var projections = IntStream.range(0, 9)
                .mapToObj(i -> mock(MetricProjection.class))
                .collect(Collectors.toList());

        when(repositoryCustom.findTop10WithFilter(period))
                .thenReturn(projections);

        assertThatThrownBy(() -> metricService.findTop10ForMetrics(period))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("To find the top 10, you need at least 10 registered and used tools");
    }

    @Test
    void should_throw_exception_when_list_is_empty() {
        var period = new PeriodRequestDTO(null, null);
        when(repositoryCustom.findTop10WithFilter(period))
                .thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> metricService.findTop10ForMetrics(period))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void should_verify_rank_is_assigned_sequentially() {
        var period = new PeriodRequestDTO(null, null);
        var projections = IntStream.range(0, 10)
                .mapToObj(i -> mock(MetricProjection.class))
                .collect(Collectors.toList());

        when(repositoryCustom.findTop10WithFilter(period))
                .thenReturn(projections);

        metricService.findTop10ForMetrics(period);

        verify(mapper).toMetricResponse(projections.get(0), period, 1);
        verify(mapper).toMetricResponse(projections.get(9), period, 10);
    }
}