package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class InventoryEntityMapperTest {

    private InventoryEntityMapper mapper;

    private ProductEntity product;
    private UserEntity user;
    private InventoryEntity inventory;

    @BeforeEach
    void setup() {
        mapper = new InventoryEntityMapper();

        product = ProductEntity.builder()
                .id(1L)
                .name("Notebook")
                .active(true)
                .build();

        user = UserEntity.builder()
                .id(10L)
                .username("Lucas Silva")
                .build();

        inventory = InventoryEntity.builder()
                .id(100L)
                .quantity(50)
                .product(product)
                .build();
    }

    @Test
    void should_map_dto_to_inventory_entity() {
        var dto = new InventoryEntityDTO(30, 1L);

        var entity = mapper
                .toEntityInventory(dto, product);

        assertEquals(30, entity.getQuantity());
        assertEquals(product, entity.getProduct());
        assertNull(entity.getId());
    }

    @Test
    void should_map_inventory_to_response_dto() {
        var response = mapper
                .toResponseEntityInventory(inventory);

        assertEquals(100L, response.id());
        assertEquals(50, response.quantity());
        assertEquals(1L, response.productId());
        assertEquals(LocalDate.now(), response.createdAt());
    }

    @Test
    void should_map_inventory_to_take_response_dto() {
        var response = mapper
                .toTakeInventoryResponse(inventory, 5);

        assertEquals(100L, response.id());
        assertEquals(5, response.quantityTaked());
        assertEquals(1L, response.productId());
        assertEquals(LocalDate.now(), response.createdAt());
    }

    @Test
    void should_map_inventory_to_return_response_dto_list() {
        var response = mapper
                .toReturnedInventoryResponse(inventory, 7);

        assertEquals(1, response.size());
        assertEquals(100L, response.getFirst().id());
        assertEquals(7, response.getFirst().quantityReturned());
        assertEquals(1L, response.getFirst().productId());
        assertEquals(LocalDate.now(), response.getFirst().createdAt());
    }

    @Test
    void should_map_to_history_entity_dto() {
        var historyDTO = mapper
                .toHistoryEntityDTO(10, user, product, InventoryAction.TAKE);

        assertEquals(10, historyDTO.quantity());
        assertEquals(user, historyDTO.user());
        assertEquals(product, historyDTO.product());
        assertEquals(InventoryAction.TAKE, historyDTO.action());
    }

    @Test
    void should_update_inventory_entity() {
        var dto = new InventoryEntityDTO(80, 1L);

        mapper.updateEntity(inventory, dto, product);

        assertEquals(80, inventory.getQuantity());
        assertEquals(product, inventory.getProduct());
    }
}
