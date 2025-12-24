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
class ToolEntityServiceTest {

    @Mock
    private ToolEntityRepository repository;

    @Mock
    private ToolEntityMapper mapper;

    @InjectMocks
    private ToolEntityService service;

    private ToolEntity tool;
    private ToolEntityDTO toolDTO;

    @BeforeEach
    void setUp() {
        tool = ToolEntity.builder()
                .id(1L)
                .name("Parafusadeira")
                .active(true)
                .currentLifeCycle(100.0)
                .usageCount(0)
                .build();

        toolDTO = new ToolEntityDTO("Parafusadeira", true);
    }

    @Test
    @DisplayName("Save success")
    void save_Success() {
        when(repository.existsByName(anyString())).thenReturn(false);
        when(mapper.toEntityTool(any())).thenReturn(tool);
        when(repository.save(any())).thenReturn(tool);

        service.save(toolDTO);

        verify(repository).save(any(ToolEntity.class));
    }

    @Test
    @DisplayName("Save duplicate error")
    void save_DuplicateError() {
        when(repository.existsByName(anyString())).thenReturn(true);

        assertThatThrownBy(() -> service.save(toolDTO))
                .isInstanceOf(DuplicateResouceException.class);
    }

    @Test
    @DisplayName("Find all active tools")
    void findAllIsActive_Success() {
        when(repository.findAllByActiveTrue()).thenReturn(List.of(tool));

        List<ToolEntityResponseDTO> result = service.findAllIsActive();

        assertThat(result).isNotNull();
        verify(repository).findAllByActiveTrue();
    }

    @Test
    @DisplayName("Update success")
    void update_Success() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(tool));
        when(repository.existsByNameAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(repository.save(any())).thenReturn(tool);

        service.update(1L, toolDTO);

        verify(mapper).updateEntity(eq(tool), eq(toolDTO));
        verify(repository).save(tool);
    }

    @Test
    @DisplayName("Disable by ID success")
    void disableById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(tool));

        service.disableById(1L);

        assertThat(tool.getActive()).isFalse();
    }

    @Test
    @DisplayName("Start usage reduces life cycle and increases count")
    void startUsage_Success() {
        service.startUsage(tool);

        assertThat(tool.getUsageCount()).isEqualTo(1);
        assertThat(tool.getCurrentLifeCycle()).isEqualTo(98.5);
        assertThat(tool.getUsageTime()).isNotNull();
    }

    @Test
    @DisplayName("Start usage damaged tool error")
    void startUsage_DamagedError() {
        tool.setCurrentLifeCycle(40.0);

        assertThatThrownBy(() -> service.startUsage(tool))
                .isInstanceOf(DamagedToolException.class);

        assertThat(tool.getActive()).isFalse();
        verify(repository).save(tool);
    }

    @Test
    @DisplayName("Return tool success and calculate time")
    void returnTool_Success() {
        tool.setUsageTime(LocalTime.now().minusMinutes(30));

        service.returnTool(tool);

        assertThat(tool.getUsageTime()).isNotNull();
        verify(repository).save(tool);
    }

    @Test
    @DisplayName("Filter by name pageable success")
    @SuppressWarnings("unchecked")
    void filterByNamePageable_Success() {
        Page<ToolEntity> page = new PageImpl<>(List.of(tool));
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<ToolEntity> result = service.filterByNamePageable("Para", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(repository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Find by ID not found error")
    void findById_NotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findToolByIdOrElseThrow(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Validate duplicate on update error")
    void validateDuplicateOnUpdate_Error() {
        when(repository.findById(1L)).thenReturn(Optional.of(tool));
        when(repository.existsByNameAndIdNot(anyString(), eq(1L))).thenReturn(true);

        assertThatThrownBy(() -> service.update(1L, toolDTO))
                .isInstanceOf(DuplicateResouceException.class);
    }

    @Test
    @DisplayName("Find all not active tools")
    void findAllIsNotActive_Success() {
        tool.setActive(false);
        when(repository.findAllByActiveFalse()).thenReturn(List.of(tool));

        List<ToolEntityResponseDTO> result = service.findAllIsNotActive();

        assertThat(result).isNotNull();
        verify(repository).findAllByActiveFalse();
    }
}