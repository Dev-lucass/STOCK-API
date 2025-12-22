package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryEntityMapperTest {

    private HistoryEntityMapper mapper;
    private UserEntity user;
    private ProductEntity product;

    @BeforeEach
    void setup() {
        mapper = new HistoryEntityMapper();

        user = UserEntity.builder()
                .id(1L)
                .username("Lucas Silva")
                .build();

        product = ProductEntity.builder()
                .id(10L)
                .name("Notebook")
                .active(true)
                .build();
    }

    @Test
    void should_map_dto_to_history_entity() {
        var dto = HistoryEntityDTO.builder()
                .user(user)
                .product(product)
                .quantity(5)
                .action(InventoryAction.RETURN)
                .build();

        var entity = mapper.toEntityHistory(dto);

        assertEquals(user, entity.getUser());
        assertEquals(product, entity.getProduct());
        assertEquals(5, entity.getQuantity());
        assertEquals(InventoryAction.RETURN, entity.getAction());
        assertEquals(user.getId(), entity.getId().getUserId());
        assertEquals(product.getId(), entity.getId().getProductId());
        assertEquals(LocalDate.now(), entity.getId().getCreatedAt());
    }

    @Test
    void should_map_history_entity_to_response_dto() {
        var entity = HistoryEntity.builder()
                .user(user)
                .product(product)
                .quantity(8)
                .action(InventoryAction.TAKE)
                .build();

        var response = mapper
                .toResponseEntityHistory(entity);

        assertEquals(user.getId(), response.userId());
        assertEquals(product.getId(), response.productId());
        assertEquals(8, response.quantity());
        assertEquals(InventoryAction.TAKE, response.action());
        assertEquals(LocalDate.now(), response.createdAt());
    }
}
