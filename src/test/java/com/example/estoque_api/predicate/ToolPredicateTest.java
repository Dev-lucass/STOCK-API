package com.example.estoque_api.predicate;

import com.example.estoque_api.dto.request.filter.ToolFilterDTO;
import com.example.estoque_api.model.QToolEntity;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ToolPredicateTest {

    @Test
    void build_WithNullFilter_ReturnsEmptyPredicate() {
        var result = ToolPredicate.build(null);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(((BooleanBuilder) result).hasValue())
        );
    }

    @Test
    void build_WithFullFilter_ReturnsCompletePredicate() {
        var filter = ToolFilterDTO.builder()
                .toolName("Drill")
                .toolActive(true)
                .inUse(false)
                .usageCount(10)
                .hourUsage(1)
                .minutesUsage(30)
                .secondsUsage(0)
                .build();

        var result = ToolPredicate.build(filter);
        var predicateString = result.toString();

        assertAll(
                () -> assertTrue(predicateString.contains("startsWithIgnoreCase(toolEntity.name,Drill)")),
                () -> assertTrue(predicateString.contains("toolEntity.active = true")),
                () -> assertTrue(predicateString.contains("toolEntity.inUse = false")),
                () -> assertTrue(predicateString.contains("toolEntity.usageCount = 10")),
                () -> assertTrue(predicateString.contains("toolEntity.usageTime >= 01:30"))
        );
    }

    @Test
    void build_WithCustomQEntity_UsesCorrectPath() {
        var filter = ToolFilterDTO.builder().toolName("Saw").build();
        var customQTool = new QToolEntity("customTool");

        var result = ToolPredicate.build(filter, customQTool);

        assertTrue(result.toString().contains("customTool.name"));
    }

    @Test
    void build_WithMidnightTime_ShouldNotAddUsageTimePredicate() {
        var filter = ToolFilterDTO.builder()
                .hourUsage(0)
                .minutesUsage(0)
                .secondsUsage(0)
                .build();

        var result = ToolPredicate.build(filter);

        assertFalse(result.toString().contains("usageTime"));
    }

    @Test
    void build_WithPartialTime_CalculatesCorrectTime() {
        var filter = ToolFilterDTO.builder()
                .minutesUsage(45)
                .build();

        var result = ToolPredicate.build(filter);
        var expectedTime = LocalTime.of(0, 45, 0);

        assertTrue(result.toString().contains("toolEntity.usageTime >= " + expectedTime));
    }
}