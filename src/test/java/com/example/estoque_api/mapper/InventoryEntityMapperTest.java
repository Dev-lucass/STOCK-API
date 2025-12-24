package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityTakeResponseDTO;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.assertj.core.api.Assertions.assertThat;

class InventoryEntityMapperTest {

    private InventoryEntityMapper mapper;
    private ToolEntity tool;
    private InventoryEntity entity;

    @BeforeEach
    void setUp() {
        mapper = new InventoryEntityMapper();

        tool = ToolEntity.builder()
                .id(100L)
                .name("Drill")
                .currentLifeCycle(85.0)
                .build();

        entity = InventoryEntity.builder()
                .id(1L)
                .inventoryId("INV-001")
                .quantityInitial(10)
                .quantityCurrent(8)
                .tool(tool)
                .build();
    }

    @Test
    @DisplayName("Should map request DTO to entity correctly")
    void shouldMapToEntityInventory() {
        InventoryEntityDTO dto = new InventoryEntityDTO(20,1L);

        InventoryEntity result = mapper.toEntityInventory(dto, tool);

        assertThat(result).isNotNull();
        assertThat(result.getQuantityInitial()).isEqualTo(20);
        assertThat(result.getTool()).isEqualTo(tool);
    }

    @Test
    @DisplayName("Should map entity to standard response DTO")
    void shouldMapToResponseEntityInventory() {
        InventoryEntityResponseDTO response = mapper.toResponseEntityInventory(entity);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(entity.getId());
        assertThat(response.inventoryId()).isEqualTo(entity.getInventoryId());
        assertThat(response.quantityInitial()).isEqualTo(entity.getQuantityInitial());
        assertThat(response.idTool()).isEqualTo(tool.getId());
        assertThat(response.createdAt()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Should map entity to take-action response DTO")
    void shouldMapToTakeInventoryResponse() {
        int quantityTaken = 2;
        int usageCount = 5;

        InventoryEntityTakeResponseDTO response = mapper.toTakeInventoryResponse(entity, quantityTaken, usageCount);

        assertThat(response).isNotNull();
        assertThat(response.quantityTaked()).isEqualTo(quantityTaken);
        assertThat(response.usageCount()).isEqualTo(usageCount);
        assertThat(response.currentLifeCycle()).isEqualTo(tool.getCurrentLifeCycle());
        assertThat(response.idTool()).isEqualTo(tool.getId());
    }

    @Test
    @DisplayName("Should map entity to return-action response DTO")
    void shouldMapToReturnedInventoryResponse() {
        int quantityReturned = 1;
        int usageCount = 6;
        LocalTime usageTime = LocalTime.of(1, 30);

        InventoryEntityReturnResponseDTO response = mapper.toReturnedInventoryResponse(entity, quantityReturned, usageCount, usageTime);

        assertThat(response).isNotNull();
        assertThat(response.quantityReturned()).isEqualTo(quantityReturned);
        assertThat(response.usageTime()).isEqualTo(usageTime);
        assertThat(response.usageCount()).isEqualTo(usageCount);
        assertThat(response.idTool()).isEqualTo(tool.getId());
    }

    @Test
    @DisplayName("Should update existing entity fields")
    void shouldUpdateEntity() {
        ToolEntity newTool = ToolEntity.builder().id(200L).build();
        int newCurrent = 15;
        int newInitial = 30;

        mapper.updateEntity(newCurrent, newInitial, newTool, entity);

        assertThat(entity.getQuantityCurrent()).isEqualTo(newCurrent);
        assertThat(entity.getQuantityInitial()).isEqualTo(newInitial);
        assertThat(entity.getTool()).isEqualTo(newTool);
    }
}