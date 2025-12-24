package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.assertj.core.api.Assertions.assertThat;

class InventoryEntityReturnResponseDTOTest {

    private Long id;
    private String inventoryId;
    private Long idTool;
    private int quantityReturned;
    private LocalTime usageTime;
    private LocalDate createdAt;

    @BeforeEach
    void setUp() {
        id = 10L;
        inventoryId = "INV-2024-ABC";
        idTool = 500L;
        quantityReturned = 2;
        usageTime = LocalTime.of(10, 0, 0);
        createdAt = LocalDate.of(2024, 1, 1);
    }

    @Test
    @DisplayName("Should verify all fields are correctly mapped through builder")
    void shouldBuildDtoWithCompleteData() {
        InventoryEntityReturnResponseDTO response = InventoryEntityReturnResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .idTool(idTool)
                .quantityReturned(quantityReturned)
                .usageTime(usageTime)
                .createdAt(createdAt)
                .build();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.inventoryId()).isEqualTo(inventoryId);
        assertThat(response.idTool()).isEqualTo(idTool);
        assertThat(response.quantityReturned()).isEqualTo(quantityReturned);
        assertThat(response.usageTime()).isEqualTo(usageTime);
        assertThat(response.createdAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should validate that two instances with same data are equal")
    void shouldVerifyValueEquality() {
        InventoryEntityReturnResponseDTO dtoA = createDefaultInstance();
        InventoryEntityReturnResponseDTO dtoB = createDefaultInstance();

        assertThat(dtoA).isEqualTo(dtoB);
        assertThat(dtoA.hashCode()).isEqualTo(dtoB.hashCode());
    }

    private InventoryEntityReturnResponseDTO createDefaultInstance() {
        return InventoryEntityReturnResponseDTO.builder()
                .id(id)
                .inventoryId(inventoryId)
                .build();
    }
}