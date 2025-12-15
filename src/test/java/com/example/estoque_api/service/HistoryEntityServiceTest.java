package com.example.estoque_api.service;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.HistoryEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HistoryEntityServiceTest {

    @Mock
    HistoryEntityRepository repository;

    @InjectMocks
    HistoryEntityService service;

    UserEntity user;
    ProductEntity product;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);

        product = new ProductEntity();
        product.setId(1L);
    }

    @Test
    void should_save_history() {
        ArgumentCaptor<HistoryEntity> captor = ArgumentCaptor.forClass(HistoryEntity.class);

        service.save(user, product, InventoryAction.TAKE, 10);

        verify(repository).save(captor.capture());

        HistoryEntity history = captor.getValue();

        assertEquals(user, history.getUser());
        assertEquals(product, history.getProduct());
        assertEquals(InventoryAction.TAKE, history.getAction());
        assertEquals(10, history.getQuantity());
        assertNotNull(history.getId());
    }

    @Test
    void should_find_all_history() {
        HistoryEntity history = new HistoryEntity(
                user,
                product,
                InventoryAction.RETURN,
                5
        );

        when(repository.findAll()).thenReturn(List.of(history));

        List<HistoryEntity> result = service.findAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }
}
