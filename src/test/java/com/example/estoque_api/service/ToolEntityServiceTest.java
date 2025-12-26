package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.ToolEntityDTO;
import com.example.estoque_api.dto.response.entity.ToolEntityResponseDTO;
import com.example.estoque_api.exceptions.DamagedToolException;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.ToolEntityMapper;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.repository.ToolEntityRepository;
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
class ToolEntityServiceTest {

    @Mock
    private ToolEntityRepository repository;

    @Mock
    private ToolEntityMapper mapper;

    @InjectMocks
    private ToolEntityService toolService;

    private ToolEntity tool;
    private ToolEntityDTO toolDTO;

    @BeforeEach
    void setUp() {
        tool = new ToolEntity();
        tool.setId(1L);
        tool.setName("Hammer");
        tool.setActive(true);
        tool.setCurrentLifeCycle(100.0);
        tool.setUsageCount(0);
        tool.setUsageTime(LocalTime.of(10, 0));

        toolDTO = new ToolEntityDTO("Hammer", true);
    }

    @Test
    void save_ShouldReturnResponse_WhenSuccess() {
        when(repository.existsByName(anyString())).thenReturn(false);
        when(mapper.toEntityTool(any())).thenReturn(tool);
        when(repository.save(any())).thenReturn(tool);
        when(mapper.toResponseEntityTool(any())).thenReturn(mock(ToolEntityResponseDTO.class));

        var result = toolService.save(toolDTO);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void save_ShouldThrowException_WhenNameExists() {
        when(repository.existsByName(anyString())).thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> toolService.save(toolDTO));
    }

    @Test
    void findAllIsActive_ShouldReturnList() {
        when(repository.findAllByActiveTrue()).thenReturn(List.of(tool));
        when(mapper.toResponseEntityTool(any())).thenReturn(mock(ToolEntityResponseDTO.class));

        var result = toolService.findAllIsActive();

        assertEquals(1, result.size());
    }

    @Test
    void findAllIsNotActive_ShouldReturnList() {
        tool.setActive(false);
        when(repository.findAllByActiveFalse()).thenReturn(List.of(tool));
        when(mapper.toResponseEntityTool(any())).thenReturn(mock(ToolEntityResponseDTO.class));

        var result = toolService.findAllIsNotActive();

        assertEquals(1, result.size());
    }

    @Test
    void update_ShouldReturnUpdatedResponse_WhenSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(tool));
        when(repository.existsByNameAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(repository.save(any())).thenReturn(tool);
        when(mapper.toResponseEntityTool(any())).thenReturn(mock(ToolEntityResponseDTO.class));

        var result = toolService.update(1L, toolDTO);

        assertNotNull(result);
        verify(mapper).updateEntity(tool, toolDTO);
    }

    @Test
    void disableById_ShouldSetInactive_WhenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(tool));

        toolService.disableById(1L);

        assertFalse(tool.getActive());
    }

    @Test
    void startUsage_ShouldUpdateMetrics_WhenValid() {
        tool.setUsageTime(null);

        toolService.startUsage(tool);

        assertEquals(98.5, tool.getCurrentLifeCycle());
        assertEquals(1, tool.getUsageCount());
        assertNotNull(tool.getUsageTime());
    }

    @Test
    void startUsage_ShouldThrowException_WhenLifeCycleIsLow() {
        tool.setCurrentLifeCycle(40.0);

        assertThrows(DamagedToolException.class, () -> toolService.startUsage(tool));
        assertFalse(tool.getActive());
        verify(repository).save(tool);
    }

    @Test
    void returnTool_ShouldCalculateUsageTime_WhenValid() {
        tool.setUsageTime(LocalTime.now().minusMinutes(10));

        toolService.returnTool(tool);

        verify(repository).save(tool);
        assertNotNull(tool.getUsageTime());
    }

    @Test
    void returnTool_ShouldThrowException_WhenLifeCycleIsLow() {
        tool.setCurrentLifeCycle(40.0);

        assertThrows(DamagedToolException.class, () -> toolService.returnTool(tool));
    }

    @Test
    void validateToolIsInactive_ShouldReturnTrue_WhenInactive() {
        when(repository.existsByIdAndActiveFalse(1L)).thenReturn(true);

        boolean result = toolService.validateToolIsInactive(1L);

        assertTrue(result);
    }

    @Test
    void filterByNamePageable_ShouldReturnPagedResults() {
        Page<ToolEntity> page = new PageImpl<>(List.of(tool));
        when(repository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);

        var result = toolService.filterByNamePageable("Hammer", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findToolByIdOrElseThrow_ShouldThrowException_WhenNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> toolService.findToolByIdOrElseThrow(1L));
    }
}