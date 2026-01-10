package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryDTO;
import com.example.estoque_api.dto.request.filter.InventoryFilterDTO;
import com.example.estoque_api.dto.request.persist.InventoryDTO;
import com.example.estoque_api.dto.request.persist.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryTakeResponseDTO;
import com.example.estoque_api.dto.response.filter.InventoryFilterResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.InvalidQuantityException;
import com.example.estoque_api.exceptions.QuantitySoldOutException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.HistoryMapper;
import com.example.estoque_api.mapper.InventoryMapper;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.InventoryRepository;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @InjectMocks
    private InventoryService service;

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

    @Test
    void save_ValidDto_ReturnsResponse() {
        var dto = new InventoryDTO(1, 10);
        var tool = new ToolEntity();
        var entity = new InventoryEntity();
        var response = InventoryResponseDTO.builder().id(1L).build();

        when(toolService.findToolByIdOrElseThrow(dto.idTool()))
                .thenReturn(tool);
        when(repository.existsByTool(tool))
                .thenReturn(false);
        when(mapper.toEntityInventory(dto, tool))
                .thenReturn(entity);
        when(repository.save(entity))
                .thenReturn(entity);
        when(mapper.toResponseEntityInventory(entity))
                .thenReturn(response);

        var result = service.save(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void takeFromInventory_ValidRequest_ReturnsResponse() {
        var request = new TakeFromInventory(1L, 1L, 5);
        var tool = ToolEntity.builder().id(1L).currentLifeCycle(100.0).usageCount(1).build();
        var user = new UserEntity();
        var inventory = InventoryEntity.builder().id(1L).tool(tool).quantityCurrent(10).build();
        var historyDto = HistoryDTO.builder().build();
        var response = InventoryTakeResponseDTO.builder().quantityTaked(5).build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L))
                .thenReturn(user);
        when(toolService.validateToolIsInactive(anyLong()))
                .thenReturn(false);
        when(historyMapper.buildHistoryDto(anyInt(), any(), any(), any(), anyLong(), anyDouble()))
                .thenReturn(historyDto);
        when(mapper.toTakeInventoryResponse(any(), anyInt(), anyInt()))
                .thenReturn(response);

        var result = service.takeFromInventory(request);

        assertNotNull(result);
        verify(repository).save(inventory);
    }

    @Test
    void takeFromInventory_QuantitySoldOut_ThrowsException() {
        var request = new TakeFromInventory(1L, 1L, 5);
        var tool = ToolEntity.builder().id(1L).currentLifeCycle(100.0).build();
        var inventory = InventoryEntity.builder().tool(tool).quantityCurrent(0).build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L))
                .thenReturn(new UserEntity());

        assertThrows(QuantitySoldOutException.class, () -> service.takeFromInventory(request));
    }

    @Test
    void returnFromInventory_ValidRequest_ReturnsResponse() {
        var request = new TakeFromInventory(1L, 1L, 5);
        var tool = ToolEntity.builder()
                .id(1L)
                .lastUsageStart(LocalTime.now().minusHours(1))
                .currentLifeCycle(100.0)
                .usageCount(1)
                .build();
        var user = new UserEntity();
        var inventory = InventoryEntity.builder().tool(tool).quantityCurrent(5).quantityInitial(10).build();
        var response = InventoryReturnResponseDTO.builder().build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L))
                .thenReturn(user);
        when(historyService.validateUserTakedFromInventory(user, InventoryAction.TAKE))
                .thenReturn(true);
        when(mapper.toReturnedInventoryResponse(any(), anyInt(), anyInt(), any()))
                .thenReturn(response);

        var result = service.returnFromInventory(request);

        assertNotNull(result);
        verify(repository, atLeastOnce()).save(inventory);
    }

    @Test
    void returnFromInventory_UserDidNotTake_ThrowsException() {
        var request = new TakeFromInventory(1L, 1L, 5);
        var user = new UserEntity();
        var tool = ToolEntity.builder().build();
        var inventory = InventoryEntity.builder().tool(tool).build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L))
                .thenReturn(user);
        when(historyService.validateUserTakedFromInventory(user, InventoryAction.TAKE))
                .thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.returnFromInventory(request));
    }

    @Test
    void findAll_ValidParams_ReturnsPage() {
        var filter = InventoryFilterDTO.builder().build();
        var pageable = mock(Pageable.class);
        var entity = new InventoryEntity();
        var page = new PageImpl<>(List.of(entity));
        var response = InventoryFilterResponseDTO.builder().build();

        when(repository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(page);
        when(mapper.toFilterResponse(entity))
                .thenReturn(response);

        var result = service.findAll(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void validateQuantityTaken_InvalidAmount_ThrowsException() {
        assertAll(
                () -> assertThrows(InvalidQuantityException.class, () -> service.validateQuantityTaken(0, 10)),
                () -> assertThrows(InvalidQuantityException.class, () -> service.validateQuantityTaken(15, 10))
        );
    }
}