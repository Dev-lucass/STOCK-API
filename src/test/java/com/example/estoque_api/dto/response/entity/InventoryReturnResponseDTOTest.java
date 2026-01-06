package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class InventoryReturnResponseDTOTest {

    private Long id;
    private UUID inventoryId;
    private Long idTool;
    private int quantityReturned;
    private LocalTime usageTime;
    private LocalDateTime createdAt;

    @BeforeEach
    void setUp() {
        id = 10L;
        inventoryId = UUID.randomUUID();
        idTool = 500L;
        quantityReturned = 2;
        usageTime = LocalTime.of(10, 0, 0);
        createdAt = LocalDateTime.now();
    }

    @Test
    @DisplayName("Should verify all fields are correctly mapped through builder")
    void shouldBuildDtoWithCompleteData() {
        InventoryReturnResponseDTO response = InventoryReturnResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .toolId(idTool)
                .quantityReturned(quantityReturned)
                .usageTime(usageTime)
                .returnToolAt(createdAt)
                .build();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.inventoryId()).isEqualTo(inventoryId);
        assertThat(response.toolId()).isEqualTo(idTool);
        assertThat(response.quantityReturned()).isEqualTo(quantityReturned);
        assertThat(response.usageTime()).isEqualTo(usageTime);
        assertThat(response.returnToolAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should validate that two instances with same data are equal")
    void shouldVerifyValueEquality() {
        InventoryReturnResponseDTO dtoA = createDefaultInstance();
        InventoryReturnResponseDTO dtoB = createDefaultInstance();

        assertThat(dtoA).isEqualTo(dtoB);
        assertThat(dtoA.hashCode()).isEqualTo(dtoB.hashCode());
    }

    private InventoryReturnResponseDTO createDefaultInstance() {
        return InventoryReturnResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .build();
    }
}