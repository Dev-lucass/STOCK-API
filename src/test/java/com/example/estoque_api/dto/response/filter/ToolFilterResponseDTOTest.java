package com.example.estoque_api.dto.response.filter;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ToolFilterResponseDTOTest {

    @Test
    void shouldBuildToolFilterResponseDTOWithAllFields() {
        var id = 1L;
        var name = "Hydraulic Jack";
        var active = true;
        var inUse = false;
        var usageCount = 15;
        var usageTime = LocalTime.of(2, 30, 45);
        var createdAt = LocalDateTime.of(2023, 10, 25, 10, 0, 0);

        var dto = ToolFilterResponseDTO.builder()
                .id(id)
                .name(name)
                .active(active)
                .inUse(inUse)
                .usageCount(usageCount)
                .usageTime(usageTime)
                .createdAt(createdAt)
                .build();

        assertAll(
                () -> assertEquals(id, dto.id()),
                () -> assertEquals(name, dto.name()),
                () -> assertTrue(dto.active()),
                () -> assertFalse(dto.inUse()),
                () -> assertEquals(usageCount, dto.usageCount()),
                () -> assertEquals(usageTime, dto.usageTime()),
                () -> assertEquals(createdAt, dto.createdAt())
        );
    }

    @Test
    void shouldVerifyRecordEqualityAndHashCode() {
        var now = LocalDateTime.now();
        var time = LocalTime.NOON;

        var dto1 = ToolFilterResponseDTO.builder()
                .id(1L)
                .name("Hammer")
                .usageTime(time)
                .createdAt(now)
                .build();

        var dto2 = ToolFilterResponseDTO.builder()
                .id(1L)
                .name("Hammer")
                .usageTime(time)
                .createdAt(now)
                .build();

        var dto3 = ToolFilterResponseDTO.builder()
                .id(2L)
                .name("Saw")
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
        var dto = ToolFilterResponseDTO.builder()
                .id(50L)
                .name("Drill")
                .usageCount(5)
                .build();

        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("id=50")),
                () -> assertTrue(toString.contains("name=Drill")),
                () -> assertTrue(toString.contains("usageCount=5"))
        );
    }
}