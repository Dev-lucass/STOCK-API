package com.example.estoque_api.controller;

import com.example.estoque_api.dto.internal.MetricDTO;
import com.example.estoque_api.dto.request.filter.PeriodRequestDTO;
import com.example.estoque_api.service.MetricService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MetricController.class)
class MetricControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MetricService metricService;

    @Test
    void should_return_ok_status_and_metrics_list() throws Exception {
        var metric = MetricDTO.builder()
                .mostUsedTool("Hammer")
                .usageCount(15)
                .top(1)
                .totalTaked(200)
                .build();

        when(metricService.findTop10ForMetrics(any(PeriodRequestDTO.class)))
                .thenReturn(List.of(metric));

        mockMvc.perform(get("/api/v1/metric")
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-01-13")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mostUsedTool").value("Hammer"))
                .andExpect(jsonPath("$[0].usageCount").value(15))
                .andExpect(jsonPath("$[0].top").value(1))
                .andExpect(jsonPath("$[0].totalTaked").value(200));

        verify(metricService).findTop10ForMetrics(any(PeriodRequestDTO.class));
    }

    @Test
    void should_return_ok_even_without_parameters() throws Exception {
        when(metricService.findTop10ForMetrics(any(PeriodRequestDTO.class)))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/metric")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}