package com.example.estoque_api.dto.response.filter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryFilterResponseDTOTest {

    @Test
    void shouldBuildInventoryFilterResponseDTOWithAllFields() {
        var id = 1L;
        var quantityInitial = 100;
        var quantityCurrent = 80;
        var toolId = 50L;
        var nameTool = "Pneumatic Drill";

        var dto = InventoryFilterResponseDTO.builder()
                .id(id)
                .quantityInitial(quantityInitial)
                .quantityCurrent(quantityCurrent)
                .toolId(toolId)
                .nameTool(nameTool)
                .build();

        assertAll(
                () -> assertEquals(id, dto.id()),
                () -> assertEquals(quantityInitial, dto.quantityInitial()),
                () -> assertEquals(quantityCurrent, dto.quantityCurrent()),
                () -> assertEquals(toolId, dto.toolId()),
                () -> assertEquals(nameTool, dto.nameTool())
        );
    }

    @Test
    void shouldVerifyRecordEqualityAndHashCode() {
        var dto1 = InventoryFilterResponseDTO.builder()
                .id(1L)
                .nameTool("Hammer")
                .build();

        var dto2 = InventoryFilterResponseDTO.builder()
                .id(1L)
                .nameTool("Hammer")
                .build();

        var dto3 = InventoryFilterResponseDTO.builder()
                .id(2L)
                .nameTool("Saw")
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
        var dto = InventoryFilterResponseDTO.builder()
                .id(100L)
                .nameTool("Wrench")
                .quantityCurrent(10)
                .build();

        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("id=100")),
                () -> assertTrue(toString.contains("nameTool=Wrench")),
                () -> assertTrue(toString.contains("quantityCurrent=10"))
        );
    }
}