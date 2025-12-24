package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.request.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityTakeResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InventoryEntityMapperTest {

    private InventoryEntityMapper mapper;
    private ProductEntity product;
    private UserEntity user;
    private InventoryEntity inventory;

    @BeforeEach
    void setUp() {
        mapper = new InventoryEntityMapper();

        product = new ProductEntity();
        product.setId(1L);

        user = new UserEntity();
        user.setId(2L);

        inventory = InventoryEntity.builder()
                .id(10L)
                .inventoryId("INV-001")
                .quantityInitial(100)
                .quantityCurrent(80)
                .product(product)
                .build();
    }

    @Test
    @DisplayName("Should map InventoryEntityDTO and ProductEntity to InventoryEntity")
    void shouldMapDtoToEntityInventory() {
        InventoryEntityDTO dto = new InventoryEntityDTO(100, 1L);
        InventoryEntity result = mapper.toEntityInventory(dto, product);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(100, result.getQuantityInitial()),
                () -> assertEquals(product, result.getProduct())
        );
    }

    @Test
    @DisplayName("Should build HistoryEntityDTO from individual parameters")
    void shouldBuildHistoryDto() {
        HistoryEntityDTO result = mapper.buildHistoryDto(10, InventoryAction.TAKE, product, user, "INV-001");

        assertAll(
                () -> assertEquals(10, result.quantityTaken()),
                () -> assertEquals(InventoryAction.TAKE, result.action()),
                () -> assertEquals(product, result.product()),
                () -> assertEquals(user, result.user()),
                () -> assertEquals("INV-001", result.inventoryId())
        );
    }

    @Test
    @DisplayName("Should map InventoryEntity to InventoryEntityResponseDTO")
    void shouldMapToResponseEntityInventory() {
        InventoryEntityResponseDTO result = mapper.toResponseEntityInventory(inventory);

        assertAll(
                () -> assertEquals(inventory.getId(), result.id()),
                () -> assertEquals(inventory.getInventoryId(), result.inventoryId()),
                () -> assertEquals(inventory.getQuantityInitial(), result.quantityInitial()),
                () -> assertEquals(inventory.getQuantityCurrent(), result.quantityCurrent()),
                () -> assertEquals(product.getId(), result.productId()),
                () -> assertEquals(LocalDate.now(), result.createdAt())
        );
    }

    @Test
    @DisplayName("Should map InventoryEntity to InventoryEntityTakeResponseDTO")
    void shouldMapToTakeInventoryResponse() {
        int quantityTaked = 20;
        InventoryEntityTakeResponseDTO result = mapper.toTakeInventoryResponse(inventory, quantityTaked);

        assertAll(
                () -> assertEquals(inventory.getId(), result.id()),
                () -> assertEquals(quantityTaked, result.quantityTaked()),
                () -> assertEquals(inventory.getQuantityCurrent(), result.quantityCurrent()),
                () -> assertEquals(LocalDate.now(), result.createdAt())
        );
    }

    @Test
    @DisplayName("Should map InventoryEntity to InventoryEntityReturnResponseDTO")
    void shouldMapToReturnedInventoryResponse() {
        int quantityReturned = 5;
        InventoryEntityReturnResponseDTO result = mapper.toReturnedInventoryResponse(inventory, quantityReturned);

        assertAll(
                () -> assertEquals(inventory.getId(), result.id()),
                () -> assertEquals(quantityReturned, result.quantityReturned()),
                () -> assertEquals(inventory.getQuantityCurrent(), result.quantityCurrent()),
                () -> assertEquals(LocalDate.now(), result.createdAt())
        );
    }

    @Test
    @DisplayName("Should map TakeFromInventory to HistoryEntityDTO with correct parameters")
    void shouldMapToHistoryEntityDTO() {
        TakeFromInventory take = new TakeFromInventory(2L, "INV-001", 10, 10);
        HistoryEntityDTO result = mapper.toHistoryEntityDTO(take, user, product, InventoryAction.TAKE);

        assertAll(
                () -> assertEquals(take.quantityTaken(), result.quantityTaken()),
                () -> assertEquals(take.inventoryId(), result.inventoryId()),
                () -> assertEquals(user, result.user()),
                () -> assertEquals(product, result.product()),
                () -> assertEquals(InventoryAction.TAKE, result.action())
        );
    }

    @Test
    @DisplayName("Should update existing InventoryEntity from DTO")
    void shouldUpdateEntity() {
        InventoryEntityDTO dto = new InventoryEntityDTO(200, 1L);
        mapper.updateEntity(inventory, dto, product);

        assertAll(
                () -> assertEquals(200, inventory.getQuantityInitial()),
                () -> assertEquals(200, inventory.getQuantityCurrent()),
                () -> assertEquals(product, inventory.getProduct())
        );
    }
}