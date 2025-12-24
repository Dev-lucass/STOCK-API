package com.example.estoque_api.dto.response.entity;

import com.example.estoque_api.enums.InventoryAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HistoryEntityResponseDTOTest {

    private Long historyId;
    private String inventoryId;
    private Long userId;
    private Long idTool;
    private InventoryAction takeAction;
    private InventoryAction returnAction;
    private int quantity;
    private LocalDate createdAt;

    @BeforeEach
    void setUp() {
        historyId = 1L;
        inventoryId = "INV-123";
        userId = 10L;
        idTool = 50L;
        takeAction = InventoryAction.TAKE;
        returnAction = InventoryAction.RETURN;
        quantity = 5;
        createdAt = LocalDate.now();
    }

    @Test
    @DisplayName("Should successfully instantiate the record using builder and return correct values")
    void shouldCreateHistoryEntityResponseDTOUsingBuilder() {
        HistoryEntityResponseDTO dto = HistoryEntityResponseDTO.builder()
                .historyId(historyId)
                .inventoryId(inventoryId)
                .userId(userId)
                .idTool(idTool)
                .action(takeAction)
                .quantityTaken(quantity)
                .createdAt(createdAt)
                .build();

        assertAll(
                () -> assertEquals(historyId, dto.historyId()),
                () -> assertEquals(inventoryId, dto.inventoryId()),
                () -> assertEquals(userId, dto.userId()),
                () -> assertEquals(idTool, dto.idTool()),
                () -> assertEquals(takeAction, dto.action()),
                () -> assertEquals(quantity, dto.quantityTaken()),
                () -> assertEquals(createdAt, dto.createdAt())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        HistoryEntityResponseDTO dto1 = new HistoryEntityResponseDTO(historyId, inventoryId, userId, idTool, returnAction, quantity, createdAt);
        HistoryEntityResponseDTO dto2 = new HistoryEntityResponseDTO(historyId, inventoryId, userId, idTool, returnAction, quantity, createdAt);
        HistoryEntityResponseDTO dto3 = new HistoryEntityResponseDTO(99L, "OTHER", 2L, 2L, takeAction, 10, createdAt);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        HistoryEntityResponseDTO dto = new HistoryEntityResponseDTO(historyId, inventoryId, userId, idTool, takeAction, quantity, createdAt);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("historyId=" + historyId)),
                () -> assertTrue(toString.contains("inventoryId=" + inventoryId)),
                () -> assertTrue(toString.contains("action=TAKE")),
                () -> assertTrue(toString.contains("quantityTaken=" + quantity))
        );
    }
}