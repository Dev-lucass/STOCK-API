package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.response.entity.HistoryEntityResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.mapper.HistoryEntityMapper;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.HistoryId;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.HistoryEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HistoryEntityServiceTest {

    @Mock
    HistoryEntityRepository repository;

    @Mock
    HistoryEntityMapper mapper;

    @InjectMocks
    HistoryEntityService service;

    HistoryEntity history;
    HistoryEntityDTO dto;
    UserEntity user;
    ProductEntity product;
    HistoryEntityResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);

        responseDTO = HistoryEntityResponseDTO.builder()
                .quantity(20)
                .productId(1L)
                .userId(1L)
                .createdAt(LocalDate.now())
                .build();

        product = new ProductEntity();
        product.setId(1L);

        var historyId = HistoryId.builder()
                .productId(1L)
                .userId(1L)
                .createdAt(LocalDate.now())
                .build();

        history = HistoryEntity.builder()
                .id(historyId)
                .user(user)
                .product(product)
                .action(InventoryAction.TAKE)
                .build();

        dto = HistoryEntityDTO.builder()
                .user(user)
                .product(product)
                .action(InventoryAction.TAKE)
                .build();
    }

    @Test
    void should_save_history() {
        when(mapper.toEntityHistory(Mockito.any()))
                .thenReturn(history);

        when(repository.save(Mockito.any(HistoryEntity.class)))
                .thenReturn(history);

        service.save(dto);
        verify(repository, times(1)).save(history);
    }

    @Test
    void should_find_all_history() {
        when(mapper.toResponseEntityHistory(Mockito.any()))
                .thenReturn(responseDTO);

        when(repository.findAll())
                .thenReturn(List.of(history));

        var result = service.findAll();

        assertEquals(1,result.size());
        assertNotNull(result);
        verify(repository, times(1)).findAll();
    }
}
