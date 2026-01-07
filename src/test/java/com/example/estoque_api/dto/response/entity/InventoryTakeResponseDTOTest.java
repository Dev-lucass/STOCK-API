package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class InventoryTakeResponseDTOTest {

    private Long id, idTool;
    private UUID inventoryId;
    private int quantityTaked, quantityCurrent, quantityInitial, usageCount;
    private double currentLifeCycle;
    private LocalDateTime createdAt;

    @BeforeEach
    void setUp() {
        id = 1L;
        inventoryId = UUID.randomUUID();
        quantityTaked = 10;
        quantityCurrent = 90;
        quantityInitial = 100;
        idTool = 50L;
        currentLifeCycle = 0.95;
        usageCount = 150;
        createdAt = LocalDateTime.now();
    }

    @Test
    @DisplayName("Should successfully instantiate TakeResponseDTO via builder")
    void shouldCreateDtoWithAllFields() {
        var response = InventoryTakeResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .quantityTaked(quantityTaked)
                .quantityCurrent(quantityCurrent)
                .quantityInitial(quantityInitial)
                .toolId(idTool)
                .currentLifeCycle(currentLifeCycle)
                .usageCount(usageCount)
                .takeToolAt(createdAt)
                .build();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.inventoryId()).isEqualTo(inventoryId);
        assertThat(response.quantityTaked()).isEqualTo(quantityTaked);
        assertThat(response.quantityCurrent()).isEqualTo(quantityCurrent);
        assertThat(response.quantityInitial()).isEqualTo(quantityInitial);
        assertThat(response.toolId()).isEqualTo(idTool);
        assertThat(response.currentLifeCycle()).isEqualTo(currentLifeCycle);
        assertThat(response.usageCount()).isEqualTo(usageCount);
        assertThat(response.takeToolAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should verify record immutability and equality logic")
    void shouldMaintainEquality() {
        var firstInstance = InventoryTakeResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .quantityTaked(200)
                .quantityCurrent(100)
                .quantityInitial(300)
                .usageCount(50)
                .build();

        var secondInstance = InventoryTakeResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .quantityTaked(200)
                .quantityCurrent(100)
                .quantityInitial(300)
                .usageCount(50)
                .build();

        assertThat(firstInstance).isEqualTo(secondInstance);
        assertThat(firstInstance.hashCode()).isEqualTo(secondInstance.hashCode());
    }
}