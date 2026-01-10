package com.example.estoque_api.dto.filter;

import com.example.estoque_api.dto.request.filter.InventoryFilterDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryFilterDTOTest {

    @Test
    void shouldBuildInventoryFilterDTOWithAllFields() {
        var quantityInitial = 100;
        var quantityCurrent = 50;
        var toolName = "Screwdriver";
        var toolId = 1L;

        var dto = InventoryFilterDTO.builder()
                .quantityInitial(quantityInitial)
                .quantityCurrent(quantityCurrent)
                .toolName(toolName)
                .toolId(toolId)
                .build();

        assertAll(
                () -> assertEquals(quantityInitial, dto.quantityInitial()),
                () -> assertEquals(quantityCurrent, dto.quantityCurrent()),
                () -> assertEquals(toolName, dto.toolName()),
                () -> assertEquals(toolId, dto.toolId())
        );
    }

    @Test
    void shouldVerifyRecordEqualityAndHashCode() {
        var dto1 = InventoryFilterDTO.builder()
                .toolId(10L)
                .toolName("Pliers")
                .build();

        var dto2 = InventoryFilterDTO.builder()
                .toolId(10L)
                .toolName("Pliers")
                .build();

        var dto3 = InventoryFilterDTO.builder()
                .toolId(11L)
                .toolName("Wrench")
                .build();

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3),
                () -> assertNotEquals(dto1.hashCode(), dto3.hashCode())
        );
    }

    @Test
    void shouldVerifyToStringContainsCorrectData() {
        var dto = InventoryFilterDTO.builder()
                .toolName("Tape Measure")
                .quantityCurrent(5)
                .build();

        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("Tape Measure")),
                () -> assertTrue(toString.contains("quantityCurrent=5"))
        );
    }
}