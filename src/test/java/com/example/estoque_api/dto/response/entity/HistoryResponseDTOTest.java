package com.example.estoque_api.dto.response.entity;

import com.example.estoque_api.enums.InventoryAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HistoryResponseDTOTest {

    private Long historyId;
    private UUID inventoryId;
    private Long userId;
    private Long toolId;
    private InventoryAction takeAction;
    private InventoryAction returnAction;
    private int quantity;
    private LocalDateTime createdAt;
    private String tool;

    @BeforeEach
    void setUp() {
        historyId = 1L;
        inventoryId = UUID.randomUUID();
        userId = 10L;
        toolId = 50L;
        tool = "martelo";
        takeAction = InventoryAction.TAKE;
        returnAction = InventoryAction.RETURN;
        quantity = 5;
        createdAt = LocalDateTime.now();
    }

    @Test
    @DisplayName("Should successfully instantiate the record using builder and return correct values")
    void shouldCreateHistoryEntityResponseDTOUsingBuilder() {
        HistoryResponseDTO dto = HistoryResponseDTO.builder()
                .historyId(historyId)
                .inventoryId(inventoryId)
                .userId(userId)
                .toolId(toolId)
                .action(takeAction)
                .quantityTaken(quantity)
                .createdAt(createdAt)
                .build();

        assertAll(
                () -> assertEquals(historyId, dto.historyId()),
                () -> assertEquals(inventoryId, dto.inventoryId()),
                () -> assertEquals(userId, dto.userId()),
                () -> assertEquals(toolId, dto.toolId()),
                () -> assertEquals(takeAction, dto.action()),
                () -> assertEquals(quantity, dto.quantityTaken()),
                () -> assertEquals(createdAt, dto.createdAt())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        HistoryResponseDTO dto1 = new HistoryResponseDTO(historyId, inventoryId, userId, toolId, tool, returnAction, quantity, createdAt);
        HistoryResponseDTO dto2 = new HistoryResponseDTO(historyId, inventoryId, userId, toolId, tool, returnAction, quantity, createdAt);
        HistoryResponseDTO dto3 = new HistoryResponseDTO(99L, UUID.randomUUID(), 2L,2L, "makita", takeAction, 10, createdAt);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        HistoryResponseDTO dto = new HistoryResponseDTO(historyId,inventoryId,userId, toolId,tool,InventoryAction.TAKE,quantity,createdAt);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("historyId=" + historyId)),
                () -> assertTrue(toString.contains("inventoryId=" + inventoryId)),
                () -> assertTrue(toString.contains("action=TAKE")),
                () -> assertTrue(toString.contains("quantityTaken=" + quantity))
        );
    }
}