package com.example.estoque_api.dto.internal;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryEntityDTOTest {

    private UserEntity user;
    private ToolEntity tool;
    private UUID inventoryId;
    private InventoryAction action;
    private int quantity;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        tool = new ToolEntity();
        inventoryId = inventoryId;
        action = InventoryAction.RETURN;
        quantity = 50;
    }

    @Test
    @DisplayName("Should successfully create an instance of HistoryEntityDTO via Builder")
    void shouldCreateHistoryEntityDTOViaBuilder() {
        HistoryEntityDTO dto = HistoryEntityDTO.builder()
                .user(user)
                .inventoryId(inventoryId)
                .tool(tool)
                .action(action)
                .quantityTaken(quantity)
                .build();

        assertAll(
                () -> assertEquals(user, dto.user()),
                () -> assertEquals(inventoryId, dto.inventoryId()),
                () -> assertEquals(tool, dto.tool()),
                () -> assertEquals(action, dto.action()),
                () -> assertEquals(quantity, dto.quantityTaken())
        );
    }

    @Test
    @DisplayName("Should ensure data integrity within the Record")
    void shouldEnsureDataIntegrity() {
        HistoryEntityDTO dto = new HistoryEntityDTO(
                user,
                inventoryId,
                tool,
                action,
                quantity
        );

        assertAll(
                () -> assertEquals(inventoryId, dto.inventoryId()),
                () -> assertEquals(quantity, dto.quantityTaken()),
                () -> assertEquals(user, dto.user())
        );
    }
}