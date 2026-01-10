package com.example.estoque_api.dto.filter;

import com.example.estoque_api.dto.request.filter.ToolFilterDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolFilterDTOTest {

    @Test
    void shouldBuildToolFilterDTOWithAllFields() {
        var toolName = "Electric Saw";
        var toolActive = true;
        var inUse = true;
        var usageCount = 42;
        var hourUsage = 10;
        var minutesUsage = 15;
        var secondsUsage = 30;

        var dto = ToolFilterDTO.builder()
                .toolName(toolName)
                .toolActive(toolActive)
                .inUse(inUse)
                .usageCount(usageCount)
                .hourUsage(hourUsage)
                .minutesUsage(minutesUsage)
                .secondsUsage(secondsUsage)
                .build();

        assertAll(
                () -> assertEquals(toolName, dto.toolName()),
                () -> assertEquals(toolActive, dto.toolActive()),
                () -> assertEquals(inUse, dto.inUse()),
                () -> assertEquals(usageCount, dto.usageCount()),
                () -> assertEquals(hourUsage, dto.hourUsage()),
                () -> assertEquals(minutesUsage, dto.minutesUsage()),
                () -> assertEquals(secondsUsage, dto.secondsUsage())
        );
    }

    @Test
    void shouldVerifyRecordEqualityAndHashCode() {
        var dto1 = ToolFilterDTO.builder()
                .toolName("Drill")
                .usageCount(5)
                .build();

        var dto2 = ToolFilterDTO.builder()
                .toolName("Drill")
                .usageCount(5)
                .build();

        var dto3 = ToolFilterDTO.builder()
                .toolName("Grinder")
                .usageCount(2)
                .build();

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3),
                () -> assertNotEquals(dto1.hashCode(), dto3.hashCode())
        );
    }

    @Test
    void shouldVerifyToStringContainsFieldValues() {
        var dto = ToolFilterDTO.builder()
                .toolName("Multi-tool")
                .usageCount(100)
                .build();

        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("Multi-tool")),
                () -> assertTrue(toString.contains("usageCount=100"))
        );
    }
}