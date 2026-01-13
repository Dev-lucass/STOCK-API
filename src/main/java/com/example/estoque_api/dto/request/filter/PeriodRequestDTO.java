package com.example.estoque_api.dto.request.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public record PeriodRequestDTO(
        @DateTimeFormat(pattern = "dd/MM/yyyy")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDate startDate,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @DateTimeFormat(pattern = "dd/MM/yyyy")
        LocalDate endDate){}