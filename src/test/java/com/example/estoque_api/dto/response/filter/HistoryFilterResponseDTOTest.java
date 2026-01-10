package com.example.estoque_api.dto.response.filter;

import com.example.estoque_api.enums.InventoryAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryFilterResponseDTOTest {

    @Test
    void shouldBuildHistoryFilterResponseDTOWithAllFields() {
        var id = 1L;
        var inventoryId = 10L;
        var action = InventoryAction.TAKE;
        var user = UserFilterResponseDTO.builder().id(1L).username("john.doe").build();
        var tool = ToolFilterResponseDTO.builder().id(5L).name("Hammer").build();

        var dto = HistoryFilterResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .action(action)
                .user(user)
                .tool(tool)
                .build();

        assertAll(
                () -> assertEquals(id, dto.id()),
                () -> assertEquals(inventoryId, dto.inventoryId()),
                () -> assertEquals(action, dto.action()),
                () -> assertEquals(user, dto.user()),
                () -> assertEquals(tool, dto.tool())
        );
    }

    @Test
    void shouldVerifyRecordEqualityAndHashCode() {
        var user = UserFilterResponseDTO.builder().id(1L).build();
        var tool = ToolFilterResponseDTO.builder().id(1L).build();

        var dto1 = HistoryFilterResponseDTO.builder()
                .id(1L)
                .user(user)
                .tool(tool)
                .build();

        var dto2 = HistoryFilterResponseDTO.builder()
                .id(1L)
                .user(user)
                .tool(tool)
                .build();

        var dto3 = HistoryFilterResponseDTO.builder()
                .id(2L)
                .build();

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    void shouldVerifyToStringContainsNestedObjects() {
        var user = UserFilterResponseDTO.builder().username("worker1").build();
        var tool = ToolFilterResponseDTO.builder().name("Wrench").build();

        var dto = HistoryFilterResponseDTO.builder()
                .id(1L)
                .user(user)
                .tool(tool)
                .build();

        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("worker1")),
                () -> assertTrue(toString.contains("Wrench")),
                () -> assertTrue(toString.contains("id=1"))
        );
    }
}