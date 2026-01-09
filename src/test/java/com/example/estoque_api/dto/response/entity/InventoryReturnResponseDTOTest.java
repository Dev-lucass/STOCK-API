package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class InventoryReturnResponseDTOTest {

    private long id, idTool,inventoryId;
    private LocalTime usageTime;
    private LocalDateTime createdAt;
    private int quantityReturned, quantityInitial, quantityCurrent;

    @BeforeEach
    void setUp() {
        id = 10L;
        inventoryId = 1L;
        idTool = 500L;
        quantityReturned = 2;
        usageTime = LocalTime.of(10, 0, 0);
        createdAt = LocalDateTime.now();
        quantityCurrent = 50;
        quantityInitial = 100;
    }

    @Test
    @DisplayName("Should verify all fields are correctly mapped through builder")
    void shouldBuildDtoWithCompleteData() {
        var response = InventoryReturnResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .toolId(idTool)
                .quantityReturned(quantityReturned)
                .usageTime(usageTime)
                .returnToolAt(createdAt)
                .quantityInitial(100)
                .quantityCurrent(50)
                .usageCount(1)
                .currentLifeCycle(70.0)
                .build();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.inventoryId()).isEqualTo(inventoryId);
        assertThat(response.toolId()).isEqualTo(idTool);
        assertThat(response.quantityReturned()).isEqualTo(quantityReturned);
        assertThat(response.quantityCurrent()).isEqualTo(quantityCurrent);
        assertThat(response.quantityInitial()).isEqualTo(quantityInitial);
        assertThat(response.usageTime()).isEqualTo(usageTime);
        assertThat(response.returnToolAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should validate that two instances with same data are equal")
    void shouldVerifyValueEquality() {
        var dto1 = createDefaultInstance();
        var dto2 = createDefaultInstance();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    private InventoryReturnResponseDTO createDefaultInstance() {
        return InventoryReturnResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .quantityReturned(2)
                .quantityInitial(200)
                .quantityCurrent(120)
                .usageCount(50)
                .currentLifeCycle(70.0)
                .toolId(1L)
                .build();
    }
}