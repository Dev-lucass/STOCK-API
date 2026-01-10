package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.persist.ToolDTO;
import com.example.estoque_api.model.ToolEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ToolMapperTest {

    private ToolMapper mapper;
    private ToolDTO toolDTO;
    private ToolEntity toolEntity;

    @BeforeEach
    void setUp() {
        mapper = new ToolMapper();

        toolDTO = new ToolDTO("Keyboard Mechanical", true);

        toolEntity = ToolEntity.builder()
                .id(1L)
                .name("Mouse Optical")
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Should map ToolDTO to ToolEntity successfully")
    void shouldMapDtoToEntityTool() {
        var result = mapper.toEntityTool(toolDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(toolDTO.name(), result.getName()),
                () -> assertEquals(toolDTO.active(), result.getActive())
        );
    }

    @Test
    @DisplayName("Should map ToolEntity to ToolResponseDTO successfully")
    void shouldMapEntityToResponseEntityTool() {
        var result = mapper
                .toResponseEntityTool(toolEntity);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(toolEntity.getId(), result.id()),
                () -> assertEquals(toolEntity.getName(), result.name()),
                () -> assertEquals(toolEntity.getActive(), result.active()),
                () -> assertNotNull(result.createdAt()),
                () -> assertTrue(result.createdAt().isBefore(LocalDateTime.now().plusSeconds(1)))
        );
    }

    @Test
    @DisplayName("Should update existing ToolEntity with DTO data")
    void shouldUpdateEntity() {
        mapper.updateEntity(toolEntity, toolDTO);

        assertAll(
                () -> assertEquals(toolDTO.name(), toolEntity.getName()),
                () -> assertEquals(toolDTO.active(), toolEntity.getActive()),
                () -> assertEquals(1L, toolEntity.getId())
        );
    }
}