package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryDTO;
import com.example.estoque_api.dto.response.entity.HistoryResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.InvalidQuantityException;
import com.example.estoque_api.mapper.HistoryMapper;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.HistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @Mock
    private HistoryRepository repository;

    @Mock
    private HistoryMapper mapper;

    @InjectMocks
    private HistoryService service;

    private UserEntity user;
    private HistoryEntity historyEntity;
    private HistoryDTO historyDTO;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder().id(1L).username("admin").build();

        historyEntity = HistoryEntity.builder()
                .id(1L)
                .action(InventoryAction.TAKE)
                .quantityTaken(50)
                .inventoryId(1L)
                .build();

        historyDTO = mock(HistoryDTO.class);
    }

    @Test
    @DisplayName("Should save history entity successfully")
    void save_Success() {
        when(mapper.toEntityHistory(any(HistoryDTO.class)))
                .thenReturn(historyEntity);

        service.save(historyDTO);

        verify(repository, times(1)).save(historyEntity);
    }

    @Test
    @DisplayName("Should return true when user has history with specific action")
    void validateUserTakedFromInventory_True() {
        when(repository.existsByUserAndAction(user, InventoryAction.TAKE))
                .thenReturn(true);

        var result = service
                .validateUserTakedFromInventory(user, InventoryAction.TAKE);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should find all histories and map to response DTOs")
    void findAll_Success() {
        when(repository.findAll())
                .thenReturn(List.of(historyEntity));

        when(mapper.toResponseEntityHistory(any(HistoryEntity.class)))
                .thenReturn(mock(HistoryResponseDTO.class));

        var result = service.findAll();

        assertThat(result).hasSize(1);
        verify(repository).findAll();
    }

}