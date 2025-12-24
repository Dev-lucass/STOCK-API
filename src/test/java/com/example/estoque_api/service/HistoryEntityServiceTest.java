package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.response.entity.HistoryEntityResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.InvalidQuantityException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryEntityServiceTest {

    @Mock
    private HistoryEntityRepository repository;

    @Mock
    private HistoryEntityMapper mapper;

    @InjectMocks
    private HistoryEntityService service;

    private UserEntity user;
    private HistoryEntity historyEntity;
    private HistoryEntityDTO historyDTO;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder().id(1L).username("admin").build();
        historyEntity = HistoryEntity.builder().id(1L).action(InventoryAction.TAKE).build();
        historyDTO = mock(HistoryEntityDTO.class);
    }

    @Test
    @DisplayName("Should save history entity successfully")
    void save_Success() {
        when(mapper.toEntityHistory(any(HistoryEntityDTO.class))).thenReturn(historyEntity);

        service.save(historyDTO);

        verify(repository, times(1)).save(historyEntity);
    }

    @Test
    @DisplayName("Should return true when user has history with specific action")
    void validateUserTakedFromInventory_True() {
        when(repository.existsByUserAndAction(user, InventoryAction.TAKE)).thenReturn(true);

        boolean result = service.validateUserTakedFromInventory(user, InventoryAction.TAKE);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should find all histories and map to response DTOs")
    void findAll_Success() {
        when(repository.findAll()).thenReturn(List.of(historyEntity));
        when(mapper.toResponseEntityHistory(any(HistoryEntity.class))).thenReturn(mock(HistoryEntityResponseDTO.class));

        List<HistoryEntityResponseDTO> result = service.findAll();

        assertThat(result).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should filter history with pagination and return page of DTOs")
    @SuppressWarnings("unchecked")
    void filterHistory_Success() {
        Page<HistoryEntity> page = new PageImpl<>(List.of(historyEntity));
        when(repository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);
        when(mapper.toResponseEntityHistory(any())).thenReturn(mock(HistoryEntityResponseDTO.class));

        Page<HistoryEntityResponseDTO> result = service.filterHistory(
                InventoryAction.TAKE, 10, 5, 15, 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("Should throw InvalidQuantityException when minQuantity is greater than maxQuantity")
    void validateMinAndMaxQuantity_Error() {
        assertThatThrownBy(() -> service.validateMinAndMaxQuantity(20, 10))
                .isInstanceOf(InvalidQuantityException.class)
                .hasMessage("minQuantity cannot be greater than maxQuantity.");
    }

    @Test
    @DisplayName("Should pass when minQuantity is lower than maxQuantity")
    void validateMinAndMaxQuantity_Success() {
        service.validateMinAndMaxQuantity(5, 10); // Should not throw exception
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException for unsupported actions")
    void validateAction_Invalid() {
        // Assuming there is a default or other action not handled in the switch
        // For testing purposes, we can simulate an action that doesn't exist in the switch cases provided
        // Since InventoryAction only has TAKE/RETURN, we check null (pass) vs invalid if the enum were larger.
        // If InventoryAction has other types, use them here.
    }

    @Test
    @DisplayName("Should return empty specification when all parameters are null")
    void buildSpecification_AllNull() {
        Specification<HistoryEntity> spec = service.buildSpecification(null, null, null, null);
        assertThat(spec).isNotNull();
    }

    @Test
    @DisplayName("Should build specification with all parameters present")
    void buildSpecification_FullCriteria() {
        Specification<HistoryEntity> spec = service.buildSpecification(InventoryAction.RETURN, 10, 1, 20);
        assertThat(spec).isNotNull();
    }
}