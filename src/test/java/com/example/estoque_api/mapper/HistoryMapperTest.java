package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.internal.HistoryDTO;
import com.example.estoque_api.dto.response.entity.HistoryResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HistoryMapperTest {

    private HistoryMapper mapper;
    private UserEntity user;
    private ToolEntity tool;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        mapper = new HistoryMapper();

        user = new UserEntity();
        user.setId(10L);

        tool = new ToolEntity();
        tool.setId(50L);
        tool.setName("Hammer");

        uuid = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should map HistoryDTO to HistoryEntity successfully")
    void shouldMapDtoToEntity() {
        HistoryDTO dto = HistoryDTO.builder()
                .user(user)
                .inventoryId(uuid)
                .tool(tool)
                .quantityTaken(10)
                .action(InventoryAction.TAKE)
                .build();

        HistoryEntity entity = mapper.toEntityHistory(dto);

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
        HistoryEntity entity = HistoryEntity.builder()
                .id(1L)
                .user(user)
                .inventoryId(uuid)
                .tool(tool)
                .quantityTaken(5)
                .action(InventoryAction.RETURN)
                .build();

        HistoryResponseDTO response = mapper.toResponseEntityHistory(entity);

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