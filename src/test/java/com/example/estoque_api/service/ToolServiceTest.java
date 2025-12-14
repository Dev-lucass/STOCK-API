package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.ToolDTO;
import com.example.estoque_api.dto.response.entity.ToolResponseDTO;
import com.example.estoque_api.exceptions.DamagedToolException;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.ToolMapper;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.ToolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
class ToolServiceTest {

    @Mock
    private ToolRepository repository;

    @Mock
    private ToolMapper mapper;

    @Mock
    private HistoryService historyService;

    @InjectMocks
    private ToolService toolService;

    private UserEntity user;
    private ToolEntity tool;
    private ToolDTO toolDTO;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setUsername("joao");

        tool = new ToolEntity();
        tool.setId(1L);
        tool.setName("Hammer");
        tool.setActive(true);
        tool.setCurrentLifeCycle(100.0);
        tool.setDegradationRate(1.5);
        tool.setMinimumViableLife(10.0);
        tool.setUsageCount(0);
        tool.setUsageTime(null);

        toolDTO = new ToolDTO("Hammer", true);
    }

    @Test
    void save_ShouldReturnResponse_WhenSuccess() {
        when(repository.existsByName(anyString()))
                .thenReturn(false);

        when(mapper.toEntityTool(any()))
                .thenReturn(tool);

        when(repository.save(any()))
                .thenReturn(tool);

        when(mapper.toResponseEntityTool(any())).
                thenReturn(mock(ToolResponseDTO.class));

        var result = toolService.save(toolDTO);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void save_ShouldThrowException_WhenNameExists() {
        when(repository.existsByName(anyString()))
                .thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> toolService.save(toolDTO));
    }

    @Test
    void findAllisActive_ShouldReturnList() {
        when(repository.findAllByActiveTrue())
                .thenReturn(List.of(tool));

        when(mapper.toResponseEntityTool(any()))
                .thenReturn(mock(ToolResponseDTO.class));

        var result = toolService.findAllisActive();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void update_ShouldReturnUpdatedResponse_WhenSuccess() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(tool));

        when(repository.existsByNameAndIdNot(anyString(), anyLong()))
                .thenReturn(false);

        when(repository.save(any()))
                .thenReturn(tool);

        when(mapper.toResponseEntityTool(any()))
                .thenReturn(mock(ToolResponseDTO.class));

        var result = toolService.update(1L, toolDTO);

        assertNotNull(result);
        verify(mapper).updateEntity(tool, toolDTO);
    }

    @Test
    void disableById_ShouldSetInactive_WhenFound() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(tool));

        toolService.disableById(1L);

        assertFalse(tool.getActive());
        assertEquals(0.0, tool.getCurrentLifeCycle());
    }

    @Test
    void startUsage_ShouldUpdateMetrics_WhenValid() {
        toolService.startUsage(tool);

        assertEquals(98.5, tool.getCurrentLifeCycle());
        assertEquals(1, tool.getUsageCount());
        assertNotNull(tool.getUsageTime());
    }

    @Test
    void startUsage_ShouldThrowException_WhenLifeCycleIsLow() {
        tool.setCurrentLifeCycle(5.0);
        assertThrows(DamagedToolException.class, () -> toolService.startUsage(tool));
    }

    @Test
    void returnTool_ShouldResetUsageTime_WhenDebtIsZero() {
        tool.setUsageTime(LocalTime.now());

        when(historyService.currentDebt(user))
                .thenReturn(0);

        toolService.returnTool(tool, user);

        assertEquals(LocalTime.MIDNIGHT, tool.getUsageTime());
    }

    @Test
    void returnTool_ShouldNotResetUsageTime_WhenDebtExists() {
        var startTime = LocalTime.now();
        tool.setUsageTime(startTime);

        when(historyService.currentDebt(user))
                .thenReturn(5);

        toolService.returnTool(tool, user);

        assertEquals(startTime, tool.getUsageTime());
    }

    @Test
    void validateToolIsInactive_ShouldReturnTrue_WhenInactive() {
        when(repository.existsByIdAndActiveFalse(1L))
                .thenReturn(true);

        var result = toolService.validateToolIsInactive(1L);
        assertTrue(result);
    }

    @Test
    @SuppressWarnings("unchecked")
    void filterByNamePageable_ShouldReturnPagedResults() {
        var page = new PageImpl<>(List.of(tool));

        when(repository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(page);

        var result = toolService
                .filterByNamePageable("Hammer", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void calculateUsageTime_ShouldReturnCorrectDuration() {
        tool.setUsageTime(LocalTime.now().minusHours(2));

        var result = toolService.calculateUsageTime(tool);

        assertTrue(result.getHour() >= 2);
    }

    @Test
    void findToolByIdOrElseThrow_ShouldThrowException_WhenNotFound() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> toolService.findToolByIdOrElseThrow(1L));
    }
}