package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class InventoryResponseDTOTest {

    private Long id, idTool;
    private UUID inventoryId;
    private int quantityInitial,quantityCurrent;
    private LocalDateTime createdAt;

    @BeforeEach
    void setUp() {
        id = 1L;
        inventoryId = UUID.randomUUID();
        quantityInitial = 100;
        quantityCurrent = 85;
        idTool = 50L;
        createdAt = LocalDateTime.now();
    }

    @Test
    @DisplayName("Should create Response DTO with all fields correctly mapped")
    void shouldCreateDtoWithCompleteData() {
        var response = InventoryResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .quantityInitial(quantityInitial)
                .quantityCurrent(quantityCurrent)
                .toolId(idTool)
                .createdAt(createdAt)
                .build();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.inventoryId()).isEqualTo(inventoryId);
        assertThat(response.quantityInitial()).isEqualTo(quantityInitial);
        assertThat(response.quantityCurrent()).isEqualTo(quantityCurrent);
        assertThat(response.toolId()).isEqualTo(idTool);
        assertThat(response.createdAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should verify record equality for identical data")
    void should_Verify_Equality() {
        var dto1 = InventoryResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .quantityInitial(10)
                .quantityCurrent(2)
                .build();

        var dto2 = InventoryResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .quantityInitial(10)
                .quantityCurrent(2)
                .build();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    @DisplayName("Should verify record field accessors return correct values")
    void should_Access_Fields() {
        var response = new InventoryResponseDTO(
                id, inventoryId, quantityInitial, quantityCurrent, idTool, createdAt
        );

        assertThat(response.quantityInitial())
                .isEqualTo(100);

        assertThat(response.quantityCurrent())
                .isEqualTo(85);
    }
}