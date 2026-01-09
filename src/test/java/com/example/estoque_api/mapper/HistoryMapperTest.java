package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.internal.HistoryDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryMapperTest {

    private HistoryMapper mapper;
    private UserEntity user;
    private ToolEntity tool;
    private long inventoryId;

    @BeforeEach
    void setUp() {
        mapper = new HistoryMapper();

        user = new UserEntity();
        user.setId(10L);

        tool = new ToolEntity();
        tool.setId(50L);
        tool.setName("Hammer");

        inventoryId = 1L;
    }

    @Test
    @DisplayName("Should map HistoryDTO to HistoryEntity successfully")
    void shouldMapDtoToEntity() {
        var dto = HistoryDTO.builder()
                .user(user)
                .inventoryId(inventoryId)
                .tool(tool)
                .quantityTaken(10)
                .action(InventoryAction.TAKE)
                .currentLifeCycle(100.0)
                .build();

        var entity = mapper.toEntityHistory(dto);

        assertAll(
                () -> assertNotNull(entity),
                () -> assertEquals(dto.user(), entity.getUser()),
                () -> assertEquals(dto.inventoryId(), entity.getInventoryId()),
                () -> assertEquals(dto.tool(), entity.getTool()),
                () -> assertEquals(dto.quantityTaken(), entity.getQuantityTaken()),
                () -> assertEquals(dto.action(), entity.getAction())
        );
    }

    @Test
    @DisplayName("Should map HistoryEntity to HistoryResponseDTO successfully")
    void shouldMapEntityToResponseDto() {
        var entity = HistoryEntity.builder()
                .id(1L)
                .user(user)
                .inventoryId(inventoryId)
                .tool(tool)
                .quantityTaken(5)
                .action(InventoryAction.RETURN)
                .build();

        var response = mapper
                .toResponseEntityHistory(entity);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(entity.getId(), response.historyId()),
                () -> assertEquals(entity.getInventoryId(), response.inventoryId()),
                () -> assertEquals(user.getId(), response.userId()),
                () -> assertEquals(tool.getId(), response.toolId()),
                () -> assertEquals(entity.getQuantityTaken(), response.quantityTaken()),
                () -> assertEquals(entity.getAction(), response.action())
        );
    }
}