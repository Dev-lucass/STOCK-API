package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.response.entity.HistoryEntityResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HistoryEntityMapperTest {

    private HistoryEntityMapper mapper;
    private UserEntity user;
    private ProductEntity product;

    @BeforeEach
    void setUp() {
        mapper = new HistoryEntityMapper();

        user = new UserEntity();
        user.setId(10L);

        product = new ProductEntity();
        product.setId(50L);
    }

    @Test
    @DisplayName("Should map HistoryEntityDTO to HistoryEntity successfully")
    void shouldMapDtoToEntity() {
        HistoryEntityDTO dto = HistoryEntityDTO.builder()
                .user(user)
                .inventoryId("INV-001")
                .product(product)
                .quantityTaken(10)
                .action(InventoryAction.TAKE)
                .build();

        HistoryEntity entity = mapper.toEntityHistory(dto);

        assertAll(
                () -> assertNotNull(entity),
                () -> assertEquals(dto.user(), entity.getUser()),
                () -> assertEquals(dto.inventoryId(), entity.getInventoryId()),
                () -> assertEquals(dto.product(), entity.getProduct()),
                () -> assertEquals(dto.quantityTaken(), entity.getQuantityTaken()),
                () -> assertEquals(dto.action(), entity.getAction())
        );
    }

    @Test
    @DisplayName("Should map HistoryEntity to HistoryEntityResponseDTO successfully")
    void shouldMapEntityToResponseDto() {
        HistoryEntity entity = HistoryEntity.builder()
                .id(1L)
                .user(user)
                .inventoryId("INV-001")
                .product(product)
                .quantityTaken(5)
                .action(InventoryAction.RETURN)
                .build();

        HistoryEntityResponseDTO response = mapper.toResponseEntityHistory(entity);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(entity.getId(), response.historyId()),
                () -> assertEquals(entity.getInventoryId(), response.inventoryId()),
                () -> assertEquals(user.getId(), response.userId()),
                () -> assertEquals(product.getId(), response.productId()),
                () -> assertEquals(entity.getQuantityTaken(), response.quantityTaken()),
                () -> assertEquals(entity.getAction(), response.action()),
                () -> assertEquals(LocalDate.now(), response.createdAt())
        );
    }
}