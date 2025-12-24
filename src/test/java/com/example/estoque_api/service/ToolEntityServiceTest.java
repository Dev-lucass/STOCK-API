package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.ToolEntityDTO;
import com.example.estoque_api.dto.response.entity.ToolEntityResponseDTO;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private ToolEntityResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        tool = ToolEntity.builder()
                .id(1L)
                .name("Smartphone")
                .active(true)
                .build();

        toolDTO = new ToolEntityDTO("Smartphone", true);

        responseDTO = ToolEntityResponseDTO.builder()
                .id(1L)
                .name("Smartphone")
                .active(true)
                .createdAt(LocalDate.now())
                .build();
    }

    @Test
    @DisplayName("Should save tool successfully when name is unique")
    void shouldSaveToolSuccessfully() {
        when(repository.existsByName(toolDTO.name())).thenReturn(false);
        when(mapper.toEntityTool(toolDTO)).thenReturn(tool);
        when(repository.save(tool)).thenReturn(tool);
        when(mapper.toResponseEntityTool(tool)).thenReturn(responseDTO);

        ToolEntityResponseDTO result = service.save(toolDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(responseDTO.name(), result.name()),
                () -> verify(repository).existsByName(toolDTO.name()),
                () -> verify(repository).save(any(ToolEntity.class))
        );
    }

    @Test
    @DisplayName("Should throw DuplicateResouceException when saving tool with existing name")
    void shouldThrowExceptionWhenNameAlreadyExistsOnSave() {
        when(repository.existsByName(toolDTO.name())).thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> service.save(toolDTO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should find all active tools")
    void shouldFindAllActiveTools() {
        when(repository.findAllByActiveTrue()).thenReturn(List.of(tool));
        when(mapper.toResponseEntityTool(tool)).thenReturn(responseDTO);

        List<ToolEntityResponseDTO> results = service.findAllIsActive();

        assertAll(
                () -> assertFalse(results.isEmpty()),
                () -> assertEquals(1, results.size()),
                () -> verify(repository).findAllByActiveTrue()
        );
    }

    @Test
    @DisplayName("Should update tool successfully")
    void shouldUpdateToolSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(tool));
        when(repository.existsByNameAndIdNot(toolDTO.name(), 1L)).thenReturn(false);
        when(repository.save(tool)).thenReturn(tool);
        when(mapper.toResponseEntityTool(tool)).thenReturn(responseDTO);

        ToolEntityResponseDTO result = service.update(1L, toolDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> verify(mapper).updateEntity(tool, toolDTO),
                () -> verify(repository).save(tool)
        );
    }

    @Test
    @DisplayName("Should disable tool by setting active to false")
    void shouldDisableToolSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(tool));

        service.disableById(1L);

        assertFalse(tool.getActive());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when tool ID is invalid")
    void shouldThrowExceptionWhenIdDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findToolByIdOrElseThrow(1L));
    }
}