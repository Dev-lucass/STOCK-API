package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.InventoryDTO;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class InventoryMapperTest {

    private InventoryMapper mapper;
    private ToolEntity tool;
    private InventoryEntity entity;

    @BeforeEach
    void setUp() {
        mapper = new InventoryMapper();
        var inventoryId = UUID.randomUUID();

        tool = ToolEntity.builder()
                .id(100L)
                .name("Drill")
                .currentLifeCycle(85.0)
                .build();

        entity = InventoryEntity.builder()
                .id(1L)
                .inventoryId(inventoryId)
                .quantityInitial(10)
                .quantityCurrent(8)
                .tool(tool)
                .build();
    }

    @Test
    @DisplayName("Should map request DTO to entity correctly")
    void shouldMapToEntityInventory() {
        var dto = new InventoryDTO(20, 1L);

        var result = mapper
                .toEntityInventory(dto, tool);

        assertThat(result).isNotNull();
        assertThat(result.getQuantityInitial()).isEqualTo(20);
        assertThat(result.getTool()).isEqualTo(tool);
    }

    @Test
    @DisplayName("Should map entity to standard response DTO")
    void shouldMapToResponseEntityInventory() {
        var response = mapper
                .toResponseEntityInventory(entity);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(entity.getId());
        assertThat(response.inventoryId()).isEqualTo(entity.getInventoryId());
        assertThat(response.quantityInitial()).isEqualTo(entity.getQuantityInitial());
        assertThat(response.quantityCurrent()).isEqualTo(entity.getQuantityCurrent());
        assertThat(response.toolId()).isEqualTo(tool.getId());
        assertThat(response.createdAt()).isCloseTo(LocalDateTime.now(), within(1, SECONDS));
    }

    @Test
    @DisplayName("Should map entity to take-action response DTO")
    void shouldMapToTakeInventoryResponse() {
        var quantityTaken = 2;
        var usageCount = 5;

        var response = mapper
                .toTakeInventoryResponse(entity, quantityTaken, usageCount);

        assertThat(response).isNotNull();
        assertThat(response.quantityTaked()).isEqualTo(quantityTaken);
        assertThat(response.usageCount()).isEqualTo(usageCount);
        assertThat(response.currentLifeCycle()).isEqualTo(tool.getCurrentLifeCycle());
        assertThat(response.toolId()).isEqualTo(tool.getId());
        assertThat(response.takeToolAt()).isCloseTo(LocalDateTime.now(), within(1, SECONDS));
    }

    @Test
    @DisplayName("Should map entity to return-action response DTO")
    void shouldMapToReturnedInventoryResponse() {
        var quantityReturned = 1;
        var usageCount = 6;
        var usageTime = LocalTime.of(1, 30);

        var response = mapper
                .toReturnedInventoryResponse(entity, quantityReturned, usageCount, usageTime);

        assertThat(response).isNotNull();
        assertThat(response.quantityReturned()).isEqualTo(quantityReturned);
        assertThat(response.usageTime()).isEqualTo(usageTime);
        assertThat(response.usageCount()).isEqualTo(usageCount);
        assertThat(response.toolId()).isEqualTo(tool.getId());
        assertThat(response.returnToolAt()).isCloseTo(LocalDateTime.now(), within(1, SECONDS));
    }

    @Test
    @DisplayName("Should update existing entity fields")
    void shouldUpdateEntity() {
        var newTool = ToolEntity.builder().id(200L).build();
        var newCurrent = 15;
        var newInitial = 30;

        mapper.updateEntity(newCurrent, newInitial, newTool, entity);

        assertThat(entity.getQuantityCurrent()).isEqualTo(newCurrent);
        assertThat(entity.getQuantityInitial()).isEqualTo(newInitial);
        assertThat(entity.getTool()).isEqualTo(newTool);
    }
}