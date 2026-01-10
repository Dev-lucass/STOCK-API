package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryDTO;
import com.example.estoque_api.dto.request.persist.InventoryDTO;
import com.example.estoque_api.dto.request.persist.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryTakeResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.mapper.HistoryMapper;
import com.example.estoque_api.mapper.InventoryMapper;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository repository;
    @Mock
    private InventoryMapper mapper;
    @Mock
    private HistoryMapper historyMapper;
    @Mock
    private ToolService toolService;
    @Mock
    private UserService userService;
    @Mock
    private HistoryService historyService;

    @InjectMocks
    private InventoryService inventoryService;

    private ToolEntity tool;
    private InventoryEntity inventory;
    private UserEntity user;
    private InventoryDTO inventoryDTO;
    private final long inventoryId = 1L;

    @BeforeEach
    void setUp() {
        tool = new ToolEntity();
        tool.setId(inventoryId);
        tool.setUsageCount(5);
        tool.setCurrentLifeCycle(10.0);

        inventory = new InventoryEntity();
        inventory.setId(inventoryId);
        inventory.setTool(tool);
        inventory.setQuantityInitial(100);
        inventory.setQuantityCurrent(50);

        user = new UserEntity();
        user.setId(inventoryId);

        inventoryDTO = new InventoryDTO(10, inventoryId);
    }

    private InventoryResponseDTO createInventoryResponseDTO() {
        return new InventoryResponseDTO(inventoryId, inventoryId, 100, 50, inventoryId, LocalDateTime.now());
    }

    @Test
    @DisplayName("Should save new item in inventory successfully")
    void save_ShouldReturnResponse_WhenSuccess() {
        when(toolService.findToolByIdOrElseThrow(inventoryId)).thenReturn(tool);
        when(repository.existsByTool(tool)).thenReturn(false);
        when(mapper.toEntityInventory(any(), any())).thenReturn(inventory);
        when(repository.save(any())).thenReturn(inventory);
        when(mapper.toResponseEntityInventory(any())).thenReturn(createInventoryResponseDTO());

        var result = inventoryService.save(inventoryDTO);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Should update quantities correctly")
    void update_ShouldUpdateQuantities_WhenSuccess() {
        when(repository.findById(inventoryId)).thenReturn(Optional.of(inventory));
        when(toolService.findToolByIdOrElseThrow(inventoryId)).thenReturn(tool);
        when(repository.existsByToolAndIdNot(tool, inventoryId)).thenReturn(false);
        when(repository.save(any())).thenReturn(inventory);
        when(mapper.toResponseEntityInventory(any())).thenReturn(createInventoryResponseDTO());

        inventoryService.update(inventoryId, inventoryDTO);

        verify(mapper).updateEntity(eq(60), eq(110), eq(tool), eq(inventory));
        verify(repository).save(inventory);
    }

    @Test
    @DisplayName("Should process inventory withdrawal successfully")
    void takeFromInventory_ShouldProcessWithdrawal_WhenValid() {
        TakeFromInventory takeRequest = new TakeFromInventory(inventoryId, inventoryId, 10);

        when(repository.findById(inventoryId)).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(inventoryId)).thenReturn(user);
        when(toolService.validateToolIsInactive(inventoryId)).thenReturn(false);

        // anyLong() ensures no null unboxing issues for inventoryId
        when(historyMapper.buildHistoryDto(anyInt(), any(), any(), any(), anyLong(), anyDouble()))
                .thenReturn(mock(HistoryDTO.class));

        when(mapper.toTakeInventoryResponse(any(), anyInt(), anyInt()))
                .thenReturn(new InventoryTakeResponseDTO(1L,
                        1L,
                        100,
                        10,
                        200,
                        2L,
                        50.0,
                        1,
                        LocalDateTime.now()));

        var result = inventoryService.takeFromInventory(takeRequest);

        assertNotNull(result);
        assertEquals(40, inventory.getQuantityCurrent());
        verify(repository).save(inventory);
    }

    @Test
    @DisplayName("Should process inventory return successfully")
    void returnFromInventory_ShouldProcessReturn_WhenValid() {
        TakeFromInventory returnRequest = new TakeFromInventory(inventoryId, inventoryId, 10);

        when(repository.findById(inventoryId)).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(inventoryId)).thenReturn(user);
        when(toolService.validateToolIsInactive(inventoryId)).thenReturn(false);
        when(historyService.validateUserTakedFromInventory(user, InventoryAction.TAKE)).thenReturn(true);
        when(toolService.calculateUsageTime(tool)).thenReturn(LocalTime.of(1, 0));

        when(historyMapper.buildHistoryDto(anyInt(), any(), any(), any(), anyLong(), anyDouble()))
                .thenReturn(mock(HistoryDTO.class));

        when(mapper.toReturnedInventoryResponse(any(), anyInt(), anyInt(), any()))
                .thenReturn(new InventoryReturnResponseDTO(1L,
                        1L,
                        1L,
                        200,
                        300,
                        200,
                        60.0,
                        2,
                        LocalTime.of(1, 0),
                        LocalDateTime.now()));

        inventoryService.returnFromInventory(returnRequest);

        assertEquals(60, inventory.getQuantityCurrent());
        verify(repository).save(inventory);
        verify(toolService).returnTool(tool, user);
    }

    @Test
    @DisplayName("Should fail withdrawal if tool is inactive and cycle is high")
    void takeFromInventory_ShouldThrowException_WhenToolIsInactiveAndCycleHigh() {
        TakeFromInventory takeRequest = new TakeFromInventory(inventoryId, inventoryId, 5);
        tool.setCurrentLifeCycle(45.0);

        when(repository.findById(inventoryId)).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(inventoryId)).thenReturn(user);
        when(toolService.validateToolIsInactive(inventoryId)).thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> inventoryService.takeFromInventory(takeRequest));
    }

    @Test
//    @SuppressWarnings("unchecked")
    @DisplayName("Should filter inventory by quantity with pagination")
    void filterByQuantity_ShouldReturnPagedResults() {
        var page = new PageImpl<>(List.of(inventory));
//        when(repository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);
        when(mapper.toResponseEntityInventory(any())).thenReturn(createInventoryResponseDTO());

//        var result = inventoryService.filterByQuantity(50, 0, 10);

//        assertNotNull(result);
//        assertFalse(result.isEmpty());
    }
}