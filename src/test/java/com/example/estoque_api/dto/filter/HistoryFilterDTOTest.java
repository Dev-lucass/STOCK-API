package com.example.estoque_api.dto.filter;

import com.example.estoque_api.dto.request.filter.HistoryFilterDTO;
import com.example.estoque_api.enums.InventoryAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryFilterDTOTest {

    @Test
    void shouldBuildHistoryFilterDTOWithAllFields() {
        var inventoryId = 1L;
        var action = InventoryAction.TAKE;
        var username = "admin";
        var cpf = "12345678900";
        var userActive = true;
        var toolName = "Drill";
        var toolActive = true;
        var inUse = false;
        var usageCount = 10;

        var dto = HistoryFilterDTO.builder()
                .inventoryId(inventoryId)
                .action(action)
                .username(username)
                .cpf(cpf)
                .userActive(userActive)
                .toolName(toolName)
                .toolActive(toolActive)
                .inUse(inUse)
                .usageCount(usageCount)
                .build();

        assertAll(
                () -> assertEquals(inventoryId, dto.inventoryId()),
                () -> assertEquals(action, dto.action()),
                () -> assertEquals(username, dto.username()),
                () -> assertEquals(cpf, dto.cpf()),
                () -> assertEquals(userActive, dto.userActive()),
                () -> assertEquals(toolName, dto.toolName()),
                () -> assertEquals(toolActive, dto.toolActive()),
                () -> assertEquals(inUse, dto.inUse()),
                () -> assertEquals(usageCount, dto.usageCount())
        );
    }

    @Test
    void shouldVerifyRecordEqualityAndHashCode() {
        var dto1 = HistoryFilterDTO.builder()
                .inventoryId(1L)
                .toolName("Hammer")
                .build();

        var dto2 = HistoryFilterDTO.builder()
                .inventoryId(1L)
                .toolName("Hammer")
                .build();

        var dto3 = HistoryFilterDTO.builder()
                .inventoryId(2L)
                .toolName("Saw")
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
        var dto = HistoryFilterDTO.builder()
                .username("test_user")
                .toolName("Wrench")
                .build();

        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("test_user")),
                () -> assertTrue(toString.contains("Wrench"))
        );
    }
}