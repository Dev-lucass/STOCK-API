package com.example.estoque_api.dto.internal;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HistoryEntityDTOTest {

    @Test
    void should_create_history_entity_dto_with_builder() {
        var user = UserEntity.builder()
                .id(1L)
                .username("Lucas Silva")
                .build();

        var product = ProductEntity.builder()
                .id(10L)
                .name("Notebook")
                .active(true)
                .build();

        var dto = HistoryEntityDTO.builder()
                .user(user)
                .product(product)
                .action(InventoryAction.TAKE)
                .quantity(5)
                .build();

        assertEquals(user, dto.user());
        assertEquals(product, dto.product());
        assertEquals(InventoryAction.TAKE, dto.action());
        assertEquals(5, dto.quantity());
    }
}
