package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.request.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.*;
import com.example.estoque_api.mapper.HistoryEntityMapper;
import com.example.estoque_api.mapper.InventoryEntityMapper;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.InventoryEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryEntityServiceTest {

    @Mock
    private InventoryEntityRepository repository;

    @Mock
    private InventoryEntityMapper mapper;

    @Mock
    private HistoryEntityMapper historyMapper;

    @Mock
    private ToolEntityService toolService;

    @Mock
    private UserEntityService userService;

    @Mock
    private HistoryEntityService historyService;

    @InjectMocks
    private InventoryEntityService service;

    private ToolEntity tool;
    private InventoryEntity inventory;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        tool = ToolEntity.builder().id(1L).usageCount(5).usageTime(LocalTime.NOON).build();
        user = UserEntity.builder().id(10L).build();
        inventory = InventoryEntity.builder()
                .id(1L)
                .inventoryId("INV-UUID")
                .quantityInitial(10)
                .quantityCurrent(10)
                .tool(tool)
                .build();
    }

    @Test
    @DisplayName("Save success")
    void save_Success() {
        InventoryEntityDTO dto = new InventoryEntityDTO(10, 1L);
        when(toolService.findToolByIdOrElseThrow(1L)).thenReturn(tool);
        when(repository.existsByTool(tool)).thenReturn(false);
        when(mapper.toEntityInventory(any(), any())).thenReturn(inventory);
        when(repository.save(any())).thenReturn(inventory);

        service.save(dto);

        verify(repository).save(any(InventoryEntity.class));
    }

    @Test
    @DisplayName("Save duplicate tool error")
    void save_DuplicateTool() {
        InventoryEntityDTO dto = new InventoryEntityDTO(10, 1L);
        when(toolService.findToolByIdOrElseThrow(1L)).thenReturn(tool);
        when(repository.existsByTool(tool)).thenReturn(true);

        assertThatThrownBy(() -> service.save(dto))
                .isInstanceOf(DuplicateResouceException.class);
    }

    @Test
    @DisplayName("Take from inventory success")
    void take_Success() {
        TakeFromInventory request = new TakeFromInventory(10L, "INV-UUID", 5);
        when(repository.findByInventoryId(anyString())).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(anyLong())).thenReturn(user);
        when(historyMapper.buildHistoryDto(anyInt(), any(), any(), any(), anyString()))
                .thenReturn(mock(HistoryEntityDTO.class));

        service.takeFromInventory(request);

        assertThat(inventory.getQuantityCurrent()).isEqualTo(5);
        verify(toolService).startUsage(tool);
        verify(repository).save(inventory);
    }

    @Test
    @DisplayName("Take invalid quantity error")
    void take_InvalidQuantity() {
        TakeFromInventory request = new TakeFromInventory(10L, "INV-UUID", 0);
        when(repository.findByInventoryId(anyString())).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(anyLong())).thenReturn(user);

        assertThatThrownBy(() -> service.takeFromInventory(request))
                .isInstanceOf(InvalidQuantityException.class);
    }

    @Test
    @DisplayName("Take sold out error")
    void take_SoldOut() {
        inventory.setQuantityCurrent(0);
        TakeFromInventory request = new TakeFromInventory(10L, "INV-UUID", 1);
        when(repository.findByInventoryId(anyString())).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(anyLong())).thenReturn(user);

        assertThatThrownBy(() -> service.takeFromInventory(request))
                .isInstanceOf(QuantitySoldOutException.class);
    }

    @Test
    @DisplayName("Take more than available error")
    void take_NotEnoughStock() {
        TakeFromInventory request = new TakeFromInventory(10L, "INV-UUID", 15);
        when(repository.findByInventoryId(anyString())).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(anyLong())).thenReturn(user);

        assertThatThrownBy(() -> service.takeFromInventory(request))
                .isInstanceOf(InvalidQuantityException.class);
    }

    @Test
    @DisplayName("Return to inventory success")
    void return_Success() {
        inventory.setQuantityCurrent(5);
        TakeFromInventory request = new TakeFromInventory(10L, "INV-UUID", 2);
        when(repository.findByInventoryId(anyString())).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(anyLong())).thenReturn(user);
        when(historyService.validateUserTakedFromInventory(user, InventoryAction.TAKE)).thenReturn(true);
        when(historyMapper.buildHistoryDto(anyInt(), any(), any(), any(), anyString()))
                .thenReturn(mock(HistoryEntityDTO.class));

        service.returnFromInventory(request);

        assertThat(inventory.getQuantityCurrent()).isEqualTo(7);
        verify(toolService).returnTool(tool);
        verify(repository).save(inventory);
    }

    @Test
    @DisplayName("Return full restoration exception")
    void return_FullRestoration() {
        inventory.setQuantityCurrent(5);
        TakeFromInventory request = new TakeFromInventory(10L, "INV-UUID", 5);
        when(repository.findByInventoryId(anyString())).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(anyLong())).thenReturn(user);
        when(historyService.validateUserTakedFromInventory(user, InventoryAction.TAKE)).thenReturn(true);
        when(historyMapper.buildHistoryDto(anyInt(), any(), any(), any(), anyString()))
                .thenReturn(mock(HistoryEntityDTO.class));

        assertThatThrownBy(() -> service.returnFromInventory(request))
                .isInstanceOf(QuantityRestoredException.class);

        assertThat(inventory.getQuantityCurrent()).isEqualTo(10);
        verify(repository).save(inventory);
    }

    @Test
    @DisplayName("Return user never took item error")
    void return_UserNeverTookItem() {
        TakeFromInventory request = new TakeFromInventory(10L, "INV-UUID", 1);
        when(repository.findByInventoryId(anyString())).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(anyLong())).thenReturn(user);
        when(historyService.validateUserTakedFromInventory(user, InventoryAction.TAKE)).thenReturn(false);

        assertThatThrownBy(() -> service.returnFromInventory(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Update success")
    void update_Success() {
        InventoryEntityDTO dto = new InventoryEntityDTO(5, 1L);
        when(repository.findById(anyLong())).thenReturn(Optional.of(inventory));
        when(toolService.findToolByIdOrElseThrow(anyLong())).thenReturn(tool);
        when(repository.save(any())).thenReturn(inventory);

        service.update(1L, dto);

        verify(mapper).updateEntity(eq(15), eq(15), eq(tool), eq(inventory));
        verify(repository).save(inventory);
    }

    @Test
    @DisplayName("Filter by quantity success")
    @SuppressWarnings("unchecked")
    void filter_Success() {
        Page<InventoryEntity> page = new PageImpl<>(List.of(inventory));
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<InventoryEntityResponseDTO> result = service.filterByQuantity(10, 0, 10);

        assertThat(result).isNotNull();
        verify(repository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Find by ID not found error")
    void findById_NotFound() {
        when(repository.findByInventoryId("INVALID")).thenReturn(Optional.empty());
        TakeFromInventory request = new TakeFromInventory(10L, "INVALID", 1);

        assertThatThrownBy(() -> service.takeFromInventory(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Find all active tools")
    void findAllByToolIsActive_Success() {
        when(repository.findAllByToolActiveTrue()).thenReturn(List.of(inventory));

        List<InventoryEntityResponseDTO> result = service.findAllByToolIsActive();

        assertThat(result).isNotNull();
        verify(repository).findAllByToolActiveTrue();
    }
}