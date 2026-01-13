package com.example.estoque_api.controller;

import com.example.estoque_api.dto.internal.MetricDTO;
import com.example.estoque_api.dto.request.filter.PeriodRequestDTO;
import com.example.estoque_api.service.MetricService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/metric")
public class MetricController {

    private final MetricService service;

    @GetMapping
    public List<MetricDTO> getMetrics(@Valid PeriodRequestDTO period) {
        return service.findTop10ForMetrics(period);
    }
}
