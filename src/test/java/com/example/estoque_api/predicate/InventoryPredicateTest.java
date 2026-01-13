package com.example.estoque_api.predicate;

import com.example.estoque_api.dto.request.filter.InventoryFilterDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryPredicateTest {

    @Test
    void build_WithNullFilter_ReturnsEmptyPredicate() {
        var result = InventoryPredicate.build(null);

        assertNotNull(result);
    }

    @Test
    void build_WithFullFilter_ReturnsCompletePredicate() {
        var filter = InventoryFilterDTO.builder()
                .quantityInitial(100)
                .quantityCurrent(50)
                .toolId(10L)
                .toolName("Drill")
                .build();

        var result = InventoryPredicate.build(filter);
        var predicateString = result.toString();

        assertAll(
                () -> assertTrue(predicateString.contains("inventoryEntity.quantityInitial = 100")),
                () -> assertTrue(predicateString.contains("inventoryEntity.quantityCurrent = 50")),
                () -> assertTrue(predicateString.contains("inventoryEntity.tool.id = 10")),
                () -> assertTrue(predicateString.contains("startsWithIgnoreCase(inventoryEntity.tool.name,Drill)"))
        );
    }

    @Test
    void build_WithBlankToolName_ShouldNotAddToolNamePredicate() {
        var filter = InventoryFilterDTO.builder()
                .toolName("   ")
                .build();

        var result = InventoryPredicate.build(filter);

        assertFalse(result.toString().contains("tool.name"));
    }
}