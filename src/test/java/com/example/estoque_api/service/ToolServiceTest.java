package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.filter.ToolFilterDTO;
import com.example.estoque_api.dto.request.persist.ToolDTO;
import com.example.estoque_api.dto.response.entity.ToolResponseDTO;
import com.example.estoque_api.dto.response.filter.ToolFilterResponseDTO;
import com.example.estoque_api.exceptions.DamagedToolException;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.exceptions.ToolInUseException;
import com.example.estoque_api.mapper.ToolMapper;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.ToolRepository;
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
class ToolServiceTest {

    @InjectMocks
    private ToolService service;

    @Mock
    private ToolRepository repository;

    @Mock
    private ToolMapper mapper;

    @Mock
    private HistoryService historyService;

    @Test
    void save_ValidDto_ReturnsResponse() {
        var dto = ToolDTO.builder().name("Hammer").build();
        var entity = new ToolEntity();
        var response = ToolResponseDTO.builder().id(1L).build();

        when(repository.existsByName(anyString()))
                .thenReturn(false);
        when(mapper.toEntityTool(dto))
                .thenReturn(entity);
        when(repository.save(entity))
                .thenReturn(entity);
        when(mapper.toResponseEntityTool(entity))
                .thenReturn(response);

        var result = service.save(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void save_DuplicateName_ThrowsException() {
        var dto = ToolDTO.builder().name("Hammer").build();

        when(repository.existsByName("Hammer"))
                .thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> service.save(dto));
    }

    @Test
    void startUsage_ToolIsDamaged_ThrowsException() {
        var tool = ToolEntity.builder()
                .currentLifeCycle(10.0)
                .minimumViableLife(20.0)
                .build();

        assertThrows(DamagedToolException.class, () -> service.startUsage(tool));
    }

    @Test
    void startUsage_ValidTool_UpdatesFields() {
        var tool = ToolEntity.builder()
                .name("Saw")
                .currentLifeCycle(100.0)
                .minimumViableLife(20.0)
                .degradationRate(5.0)
                .usageCount(0)
                .build();

        service.startUsage(tool);

        assertAll(
                () -> assertEquals(95.0, tool.getCurrentLifeCycle()),
                () -> assertEquals(1, tool.getUsageCount()),
                () -> assertNotNull(tool.getLastUsageStart())
        );
        verify(repository).save(tool);
    }

    @Test
    void disableById_ToolInUse_ThrowsException() {
        var tool = ToolEntity.builder().inUse(true).build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(tool));

        assertThrows(ToolInUseException.class, () -> service.disableById(1L));
    }

    @Test
    void returnTool_DebtIsZero_ResetsToolState() {
        var tool = ToolEntity.builder()
                .lastUsageStart(LocalTime.now().minusHours(1))
                .inUse(true)
                .build();
        var user = new UserEntity();

        when(historyService.currentDebt(user))
                .thenReturn(0);

        service.returnTool(tool, user);

        assertAll(
                () -> assertFalse(tool.getInUse()),
                () -> assertNull(tool.getLastUsageStart()),
                () -> assertNotNull(tool.getUsageTime())
        );
        verify(repository).save(tool);
    }

    @Test
    void takeTool_UpdatesInventoryAndToolStatus() {
        var tool = new ToolEntity();
        var inventory = InventoryEntity.builder().tool(tool).build();

        service.takeTool(inventory, 5);

        assertAll(
                () -> assertEquals(5, inventory.getQuantityCurrent()),
                () -> assertTrue(tool.getInUse())
        );
    }

    @Test
    void findAll_ValidParams_ReturnsPage() {
        var filter = ToolFilterDTO.builder().build();
        var pageable = mock(Pageable.class);
        var entity = new ToolEntity();
        var page = new PageImpl<>(List.of(entity));
        var response = ToolFilterResponseDTO.builder().build();

        when(repository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(page);
        when(mapper.toFilterResponse(entity))
                .thenReturn(response);

        var result = service.findAll(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findToolByIdOrElseThrow_NotFound_ThrowsException() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findToolByIdOrElseThrow(1L));
    }
}