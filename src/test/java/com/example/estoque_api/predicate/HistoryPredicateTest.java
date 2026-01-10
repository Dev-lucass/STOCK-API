package com.example.estoque_api.predicate;

import com.example.estoque_api.dto.request.filter.HistoryFilterDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryPredicateTest {

    @Test
    void build_WithFullFilter_ReturnsCompletePredicate() {
        var filter = HistoryFilterDTO.builder()
                .inventoryId(1L)
                .action(InventoryAction.TAKE)
                .username("john")
                .toolName("Hammer")
                .usageCount(5)
                .build();

        var result = HistoryPredicate.build(filter);
        var predicateString = result.toString().toLowerCase();

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(predicateString.contains("action = take"), "Deveria conter a ação"),
                () -> assertTrue(predicateString.contains("username = john") || predicateString.contains("username,john"), "Deveria conter o username"),
                () -> assertTrue(predicateString.contains("name") && predicateString.contains("hammer"), "Deveria conter o nome da ferramenta"),
                () -> assertTrue(predicateString.contains("usagecount = 5"), "Deveria conter o contador de uso"),
                () -> assertTrue(predicateString.contains("id = 1"), "Deveria conter o ID")
        );
    }

    @Test
    void build_WithNullFilter_ReturnsEmptyPredicate() {
        var result = HistoryPredicate.build(null);
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(((BooleanBuilder) result).hasValue())
        );
    }

    @Test
    void build_WithInventoryId_ReturnsInventoryPredicate() {
        var filter = HistoryFilterDTO.builder()
                .inventoryId(10L)
                .build();

        var result = HistoryPredicate.build(filter);

        assertTrue(result.toString().contains("10"), "Deveria conter o ID 10 no predicado");
    }
}