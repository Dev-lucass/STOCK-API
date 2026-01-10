package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryDTO;
import com.example.estoque_api.dto.request.filter.HistoryFilterDTO;
import com.example.estoque_api.dto.response.filter.HistoryFilterResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.InvalidQuantityException;
import com.example.estoque_api.exceptions.userMustStillReturnBeforeBeingDeactivated;
import com.example.estoque_api.mapper.HistoryMapper;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.HistoryRepository;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @InjectMocks
    private HistoryService service;

    @Mock
    private HistoryRepository repository;

    @Mock
    private HistoryMapper mapper;

    @Test
    void save_ShouldPersistHistory() {
        var dto = HistoryDTO.builder().build();
        var entity = new HistoryEntity();

        when(mapper.toEntityHistory(dto))
                .thenReturn(entity);

        service.save(dto);

        verify(repository, times(1)).save(entity);
    }

    @Test
    void validateTotalAmount_WhenQuantityIsInvalid_ShouldThrowException() {
        var user = new UserEntity();
        var tool = new ToolEntity();
        var histories = List.of(
                HistoryEntity.builder().action(InventoryAction.TAKE).quantityTaken(10).build(),
                HistoryEntity.builder().action(InventoryAction.RETURN).quantityTaken(2).build()
        );

        when(repository.findByUser(user))
                .thenReturn(histories);

        assertThrows(InvalidQuantityException.class, () ->
                service.validateTotalAmountThatTheUserMustAndResetTimeUsage(user, tool, 9));
    }

    @Test
    void currentDebt_ShouldCalculateCorrectValue() {
        var user = new UserEntity();
        var histories = List.of(
                HistoryEntity.builder().action(InventoryAction.TAKE).quantityTaken(15).build(),
                HistoryEntity.builder().action(InventoryAction.RETURN).quantityTaken(5).build()
        );

        when(repository.findByUser(user))
                .thenReturn(histories);

        var debt = service.currentDebt(user);

        assertEquals(10, debt);
    }

    @Test
    void validateUserWhetherUserOwes_WhenDebtExists_ShouldThrowException() {
        var user = new UserEntity();
        var histories = List.of(
                HistoryEntity.builder().action(InventoryAction.TAKE).quantityTaken(5).build()
        );

        when(repository.findByUser(user))
                .thenReturn(histories);

        assertThrows(userMustStillReturnBeforeBeingDeactivated.class, () ->
                service.validateUserWhetherUserOwes(user));
    }

    @Test
    void validateUserWhetherUserOwes_WhenNoDebt_ShouldNotThrowException() {
        var user = new UserEntity();
        var histories = List.of(
                HistoryEntity.builder().action(InventoryAction.TAKE).quantityTaken(5).build(),
                HistoryEntity.builder().action(InventoryAction.RETURN).quantityTaken(5).build()
        );

        when(repository.findByUser(user))
                .thenReturn(histories);

        assertDoesNotThrow(() -> service.validateUserWhetherUserOwes(user));
    }

    @Test
    void findAll_WithFilter_ShouldReturnPagedResponse() {
        var filter = HistoryFilterDTO.builder().build();
        var pageable = mock(Pageable.class);
        var entity = new HistoryEntity();
        var page = new PageImpl<>(List.of(entity));
        var response = HistoryFilterResponseDTO.builder().build();

        when(repository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(page);
        when(mapper.toFilterResponse(entity))
                .thenReturn(response);

        var result = service.findAll(filter, pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getTotalElements())
        );
    }

    @Test
    void validateUserTakedFromInventory_ShouldCallRepository() {
        var user = new UserEntity();
        var action = InventoryAction.TAKE;

        when(repository.existsByUserAndAction(user, action))
                .thenReturn(true);

        var result = service.validateUserTakedFromInventory(user, action);

        assertTrue(result);
    }
}