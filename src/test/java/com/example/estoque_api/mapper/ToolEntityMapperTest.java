package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.ToolEntityDTO;
import com.example.estoque_api.dto.response.entity.ToolEntityResponseDTO;
import com.example.estoque_api.model.ToolEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ToolEntityMapperTest {

    private ToolEntityMapper mapper;
    private ToolEntityDTO toolDTO;
    private ToolEntity toolEntity;

    @BeforeEach
    void setUp() {
        mapper = new ToolEntityMapper();
        
        toolDTO = new ToolEntityDTO("Keyboard Mechanical", true);
        
        toolEntity = ToolEntity.builder()
                .id(1L)
                .name("Mouse Optical")
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Should map ToolEntityDTO to ToolEntity successfully")
    void shouldMapDtoToEntityTool() {
        ToolEntity result = mapper.toEntityTool(toolDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(toolDTO.name(), result.getName()),
                () -> assertEquals(toolDTO.active(), result.getActive())
        );
    }

    @Test
    @DisplayName("Should map ToolEntity to ToolEntityResponseDTO successfully")
    void shouldMapEntityToResponseEntityTool() {
        ToolEntityResponseDTO result = mapper.toResponseEntityTool(toolEntity);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(toolEntity.getId(), result.id()),
                () -> assertEquals(toolEntity.getName(), result.name()),
                () -> assertEquals(toolEntity.getActive(), result.active()),
                () -> assertEquals(LocalDate.now(), result.createdAt())
        );
    }

    @Test
    @DisplayName("Should update existing ToolEntity with DTO data")
    void shouldUpdateEntity() {
        mapper.updateEntity(toolEntity, toolDTO);

        assertAll(
                () -> assertEquals(toolDTO.name(), toolEntity.getName()),
                () -> assertEquals(toolDTO.active(), toolEntity.getActive())
        );
    }
}