package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.response.entity.HistoryEntityResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.InvalidQuantityException;
import com.example.estoque_api.mapper.HistoryEntityMapper;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.HistoryEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryEntityServiceTest {

    @Mock
    private HistoryEntityRepository repository;

    @Mock
    private HistoryEntityMapper mapper;

    @InjectMocks
    private HistoryEntityService service;

    private HistoryEntity history;
    private HistoryEntityDTO historyDTO;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);

        history = new HistoryEntity();
        historyDTO = HistoryEntityDTO.builder()
                .inventoryId("INV-001")
                .quantityTaken(10)
                .action(InventoryAction.TAKE)
                .user(user)
                .build();
    }

    @Test
    @DisplayName("Should save history record successfully")
    void shouldSaveHistorySuccessfully() {
        when(mapper.toEntityHistory(historyDTO)).thenReturn(history);

        service.save(historyDTO);

        verify(repository).save(history);
        verify(mapper).toEntityHistory(historyDTO);
    }

    @Test
    @DisplayName("Should return true when user has history with specific action")
    void shouldValidateUserTakedFromInventory() {
        when(repository.existsByUserAndAction(user, InventoryAction.TAKE)).thenReturn(true);

        boolean result = service.validateUserTakedFromInventory(user, InventoryAction.TAKE);

        assertTrue(result);
        verify(repository).existsByUserAndAction(user, InventoryAction.TAKE);
    }

    @Test
    @DisplayName("Should return a list of history response DTOs")
    void shouldFindAllHistory() {
        when(repository.findAll()).thenReturn(List.of(history));
        when(mapper.toResponseEntityHistory(history)).thenReturn(mock(HistoryEntityResponseDTO.class));

        List<HistoryEntityResponseDTO> results = service.findAll();

        assertAll(
                () -> assertEquals(1, results.size()),
                () -> verify(repository).findAll()
        );
    }

    @Test
    @DisplayName("Should throw InvalidQuantityException when min is greater than max")
    void shouldThrowExceptionWhenMinGreaterThanMax() {
        assertThrows(InvalidQuantityException.class, () ->
                service.validateMinAndMaxQuantity(100, 50));
    }

    @Test
    @DisplayName("Should do nothing when action is null")
    void shouldNotThrowExceptionWhenActionIsNull() {
        // Your code uses: if (action == null) return;
        assertDoesNotThrow(() -> service.validateAction(null));
    }

    @Test
    @DisplayName("Should validate successfully for correct actions")
    void shouldValidateCorrectActions() {
        assertDoesNotThrow(() -> {
            service.validateAction(InventoryAction.TAKE);
            service.validateAction(InventoryAction.RETURN);
        });
    }
}