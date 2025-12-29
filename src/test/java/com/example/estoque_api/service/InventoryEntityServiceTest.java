package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.request.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityTakeResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.*;
import com.example.estoque_api.mapper.HistoryEntityMapper;
import com.example.estoque_api.mapper.InventoryEntityMapper;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.InventoryEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    private InventoryEntityService inventoryService;

    private ToolEntity tool;
    private InventoryEntity inventory;
    private UserEntity user;
    private InventoryEntityDTO inventoryDTO;

    @BeforeEach
    void setUp() {
        tool = new ToolEntity();
        tool.setId(1L);
        tool.setUsageCount(0);
        tool.setUsageTime(LocalTime.of(10, 0));

        inventory = new InventoryEntity();
        inventory.setId(10L);
        inventory.setTool(tool);
        inventory.setQuantityInitial(100);
        inventory.setQuantityCurrent(50);
        inventory.setInventoryId("inv-uuid-123");

        user = new UserEntity();
        user.setId(1L);

        inventoryDTO = new InventoryEntityDTO(10, 1L);
    }

    @Test
    void save_ShouldReturnResponse_WhenSuccess() {
        when(toolService.findToolByIdOrElseThrow(1L)).thenReturn(tool);
        when(repository.existsByTool(tool)).thenReturn(false);
        when(mapper.toEntityInventory(any(), any())).thenReturn(inventory);
        when(repository.save(any())).thenReturn(inventory);
        when(mapper.toResponseEntityInventory(any())).thenReturn(mock(InventoryEntityResponseDTO.class));

        var result = inventoryService.save(inventoryDTO);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void save_ShouldThrowException_WhenToolIsDuplicated() {
        when(toolService.findToolByIdOrElseThrow(1L)).thenReturn(tool);
        when(repository.existsByTool(tool)).thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> inventoryService.save(inventoryDTO));
    }

    @Test
    void update_ShouldUpdateQuantities_WhenSuccess() {
        when(repository.findById(10L)).thenReturn(Optional.of(inventory));
        when(toolService.findToolByIdOrElseThrow(1L)).thenReturn(tool);
        when(repository.existsByToolAndIdNot(tool, 10L)).thenReturn(false);
        when(repository.save(any())).thenReturn(inventory);

        inventoryService.update(10L, inventoryDTO);

        verify(mapper).updateEntity(eq(60), eq(110), eq(tool), eq(inventory));
        verify(repository).save(inventory);
    }

    @Test
    void takeFromInventory_ShouldProcessWithdrawal_WhenValid() {
        TakeFromInventory takeRequest = new TakeFromInventory(1L, "inv-uuid-123", 10);

        when(repository.findByInventoryId("inv-uuid-123")).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L)).thenReturn(user);
        when(toolService.validateToolIsInactive(anyLong())).thenReturn(false);
        when(historyMapper.buildHistoryDto(anyInt(), any(), any(), any(), anyString())).thenReturn(mock(HistoryEntityDTO.class));
        when(mapper.toTakeInventoryResponse(any(), anyInt(), anyInt())).thenReturn(mock(InventoryEntityTakeResponseDTO.class));

        inventoryService.takeFromInventory(takeRequest);

        assertEquals(40, inventory.getQuantityCurrent());
        verify(repository).save(inventory);
        verify(historyService).save(any());
        verify(toolService).startUsage(tool);
    }

    @Test
    void takeFromInventory_ShouldThrowException_WhenQuantityIsZero() {
        TakeFromInventory takeRequest = new TakeFromInventory(1L, "inv-uuid-123", 0);

        when(repository.findByInventoryId("inv-uuid-123")).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L)).thenReturn(user);

        assertThrows(InvalidQuantityException.class, () -> inventoryService.takeFromInventory(takeRequest));
    }

    @Test
    void takeFromInventory_ShouldThrowException_WhenStockIsEmpty() {
        inventory.setQuantityCurrent(0);
        TakeFromInventory takeRequest = new TakeFromInventory(1L, "inv-uuid-123", 5);

        when(repository.findByInventoryId("inv-uuid-123")).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L)).thenReturn(user);

        assertThrows(QuantitySoldOutException.class, () -> inventoryService.takeFromInventory(takeRequest));
    }

    @Test
    void returnFromInventory_ShouldProcessReturn_WhenValid() {
        TakeFromInventory returnRequest = new TakeFromInventory(1L, "inv-uuid-123", 10);

        when(repository.findByInventoryId("inv-uuid-123")).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L)).thenReturn(user);
        when(toolService.validateToolIsInactive(anyLong())).thenReturn(false);
        when(historyService.validateUserTakedFromInventory(user, InventoryAction.TAKE)).thenReturn(true);
        when(historyMapper.buildHistoryDto(anyInt(), any(), any(), any(), anyString())).thenReturn(mock(HistoryEntityDTO.class));
        when(mapper.toReturnedInventoryResponse(any(), anyInt(), anyInt(), any())).thenReturn(mock(InventoryEntityReturnResponseDTO.class));

        var result = inventoryService.returnFromInventory(returnRequest);

        assertEquals(60, inventory.getQuantityCurrent());
        verify(repository).save(inventory);
        verify(toolService).returnTool(tool);
        verify(historyService).save(any());
    }

    @Test
    void returnFromInventory_ShouldCapQuantityAtInitial_WhenReturningTooMany() {
        inventory.setQuantityCurrent(95);
        inventory.setQuantityInitial(100);
        TakeFromInventory returnRequest = new TakeFromInventory(1L, "inv-uuid-123", 10);

        when(repository.findByInventoryId("inv-uuid-123")).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L)).thenReturn(user);
        when(toolService.validateToolIsInactive(anyLong())).thenReturn(false);
        when(historyService.validateUserTakedFromInventory(user, InventoryAction.TAKE)).thenReturn(true);
        when(historyMapper.buildHistoryDto(anyInt(), any(), any(), any(), anyString())).thenReturn(mock(HistoryEntityDTO.class));

        inventoryService.returnFromInventory(returnRequest);

        assertEquals(100, inventory.getQuantityCurrent());
        verify(repository).save(inventory);
    }

    @Test
    void returnFromInventory_ShouldThrowException_WhenUserNeverPickedUp() {
        TakeFromInventory returnRequest = new TakeFromInventory(1L, "inv-uuid-123", 5);

        when(repository.findByInventoryId("inv-uuid-123")).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L)).thenReturn(user);
        when(toolService.validateToolIsInactive(anyLong())).thenReturn(false);
        when(historyService.validateUserTakedFromInventory(user, InventoryAction.TAKE)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.returnFromInventory(returnRequest));
    }

    @Test
    void returnFromInventory_ShouldThrowException_WhenQuantityIsInvalid() {
        TakeFromInventory returnRequest = new TakeFromInventory(1L, "inv-uuid-123", 0);

        when(repository.findByInventoryId("inv-uuid-123")).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L)).thenReturn(user);
        when(toolService.validateToolIsInactive(anyLong())).thenReturn(false);

        assertThrows(InvalidQuantityException.class, () -> inventoryService.returnFromInventory(returnRequest));
    }

    @Test
    void filterByQuantity_ShouldReturnPagedResults() {
        Page<InventoryEntity> page = new PageImpl<>(List.of(inventory));
        when(repository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);
        when(mapper.toResponseEntityInventory(any())).thenReturn(mock(InventoryEntityResponseDTO.class));

        var result = inventoryService.filterByQuantity(50, 0, 10);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void takeFromInventory_ShouldThrowException_WhenToolIsInactive() {
        TakeFromInventory takeRequest = new TakeFromInventory(1L, "inv-uuid-123", 5);
        when(repository.findByInventoryId("inv-uuid-123")).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L)).thenReturn(user);
        when(toolService.validateToolIsInactive(anyLong())).thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> inventoryService.takeFromInventory(takeRequest));
    }
}