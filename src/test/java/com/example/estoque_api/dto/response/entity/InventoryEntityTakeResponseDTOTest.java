package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InventoryEntityTakeResponseDTOTest {

    private Long id;
    private UUID inventoryId;
    private int quantityTaked;
    private int quantityCurrent;
    private int quantityInitial;
    private Long idTool;
    private Double currentLifeCycle;
    private int usageCount;
    private LocalDate createdAt;

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
        createdAt = LocalDate.now();
    }

    @Test
    @DisplayName("Should successfully instantiate TakeResponseDTO via builder")
    void shouldCreateDtoWithAllFields() {
        InventoryEntityTakeResponseDTO response = InventoryEntityTakeResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .quantityTaked(quantityTaked)
                .quantityCurrent(quantityCurrent)
                .quantityInitial(quantityInitial)
                .idTool(idTool)
                .currentLifeCycle(currentLifeCycle)
                .usageCount(usageCount)
                .createdAt(createdAt)
                .build();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.inventoryId()).isEqualTo(inventoryId);
        assertThat(response.quantityTaked()).isEqualTo(quantityTaked);
        assertThat(response.quantityCurrent()).isEqualTo(quantityCurrent);
        assertThat(response.quantityInitial()).isEqualTo(quantityInitial);
        assertThat(response.idTool()).isEqualTo(idTool);
        assertThat(response.currentLifeCycle()).isEqualTo(currentLifeCycle);
        assertThat(response.usageCount()).isEqualTo(usageCount);
        assertThat(response.createdAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should verify record immutability and equality logic")
    void shouldMaintainEquality() {
        InventoryEntityTakeResponseDTO firstInstance = InventoryEntityTakeResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .build();

        InventoryEntityTakeResponseDTO secondInstance = InventoryEntityTakeResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .build();

        assertThat(firstInstance).isEqualTo(secondInstance);
        assertThat(firstInstance.hashCode()).isEqualTo(secondInstance.hashCode());
    }
}