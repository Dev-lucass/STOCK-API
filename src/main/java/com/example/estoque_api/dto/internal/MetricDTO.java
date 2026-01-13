package com.example.estoque_api.dto.internal;

import com.example.estoque_api.dto.request.filter.PeriodRequestDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record MetricDTO(int top,
                        Long toolId,
                        String mostUsedTool,
                        Long userId,
                        String userUsedMost,
                        int usageCount,
                        int totalTaked,
                        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                        LocalDateTime firstUsage,
                        @JsonInclude(JsonInclude.Include.NON_EMPTY)
                        PeriodRequestDTO period){}