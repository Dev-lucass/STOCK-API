package com.example.estoque_api.predicate;

import com.example.estoque_api.dto.request.filter.ToolFilterDTO;
import com.example.estoque_api.model.QToolEntity;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.Test;

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
                .build();

        var result = ToolPredicate.build(filter);
        var predicateString = result.toString();

        assertAll(
                () -> assertTrue(predicateString.contains("startsWithIgnoreCase(toolEntity.name,Drill)")),
                () -> assertTrue(predicateString.contains("toolEntity.active = true")),
                () -> assertTrue(predicateString.contains("toolEntity.inUse = false")),
                () -> assertTrue(predicateString.contains("toolEntity.usageCount = 10"))
        );
    }

    @Test
    void build_WithCustomQEntity_UsesCorrectPath() {
        var filter = ToolFilterDTO.builder().toolName("Saw").build();
        var customQTool = new QToolEntity("customTool");

        var result = ToolPredicate.build(filter, customQTool);

        assertTrue(result.toString().contains("customTool.name"));
    }
}