package com.example.estoque_api.repository.projection;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MetricProjectionTest {

    @Test
    void should_create_metric_projection_with_correct_values() {
        LocalDateTime timestamp = LocalDateTime.of(2026, 1, 13, 10, 30, 0);
        
        MetricProjection projection = new MetricProjection(
                100L,
                "Wrench",
                timestamp,
                15,
                30,
                500L,
                "Alice Smith"
        );

        assertThat(projection.toolId()).isEqualTo(100L);
        assertThat(projection.toolName()).isEqualTo("Wrench");
        assertThat(projection.firstUsage()).isEqualTo(timestamp);
        assertThat(projection.usageCount()).isEqualTo(15);
        assertThat(projection.totalTaked()).isEqualTo(30);
        assertThat(projection.userId()).isEqualTo(500L);
        assertThat(projection.topUser()).isEqualTo("Alice Smith");
    }

    @Test
    void should_handle_null_values_in_projection() {
        MetricProjection projection = new MetricProjection(
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertThat(projection.toolId()).isNull();
        assertThat(projection.toolName()).isNull();
        assertThat(projection.firstUsage()).isNull();
        assertThat(projection.usageCount()).isNull();
        assertThat(projection.totalTaked()).isNull();
        assertThat(projection.userId()).isNull();
        assertThat(projection.topUser()).isNull();
    }

    @Test
    void should_verify_record_immutability_and_equality() {
        LocalDateTime now = LocalDateTime.now();
        
        MetricProjection p1 = new MetricProjection(1L, "Drill", now, 5, 10, 2L, "Bob");
        MetricProjection p2 = new MetricProjection(1L, "Drill", now, 5, 10, 2L, "Bob");
        MetricProjection p3 = new MetricProjection(2L, "Saw", now, 1, 1, 3L, "Charlie");

        assertThat(p1).isEqualTo(p2);
        assertThat(p1).isNotEqualTo(p3);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }
}