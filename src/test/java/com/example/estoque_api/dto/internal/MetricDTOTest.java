package com.example.estoque_api.dto.internal;

import com.example.estoque_api.dto.request.filter.PeriodRequestDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MetricDTOTest {

    @Test
    void should_build_metric_dto_successfully() {
        var now = LocalDateTime.of(2026, 1, 13, 13, 0, 0);
        var period = new PeriodRequestDTO(
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        var dto = MetricDTO.builder()
                .top(10)
                .toolId(1L)
                .mostUsedTool("Hammer")
                .userId(5L)
                .userUsedMost("John Doe")
                .usageCount(50)
                .totalTaked(100)
                .firstUsage(now)
                .period(period)
                .build();

        assertThat(dto.top()).isEqualTo(10);
        assertThat(dto.toolId()).isEqualTo(1L);
        assertThat(dto.mostUsedTool()).isEqualTo("Hammer");
        assertThat(dto.userId()).isEqualTo(5L);
        assertThat(dto.userUsedMost()).isEqualTo("John Doe");
        assertThat(dto.usageCount()).isEqualTo(50);
        assertThat(dto.totalTaked()).isEqualTo(100);
        assertThat(dto.firstUsage()).isEqualTo(now);
        assertThat(dto.period()).isEqualTo(period);
    }

    @Test
    void should_allow_null_period_when_building() {
        MetricDTO dto = MetricDTO.builder()
                .top(1)
                .mostUsedTool("Drill")
                .period(null)
                .usageCount(2)
                .totalTaked(5).build();

        assertThat(dto.period()).isNull();
        assertThat(dto.mostUsedTool()).isEqualTo("Drill");
        assertEquals(2, dto.usageCount());
        assertEquals(5, dto.totalTaked());
    }

    @Test
    void should_maintain_data_integrity_through_accessor_methods() {
        MetricDTO dto = new MetricDTO(
                5, 2L, "Saw", 10L, "Alice", 20, 40,
                LocalDateTime.now(), null
        );

        assertThat(dto.top()).isEqualTo(5);
        assertThat(dto.usageCount()).isEqualTo(20);
        assertThat(dto.userUsedMost()).isEqualTo("Alice");
    }
}