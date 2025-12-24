package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.ToolEntityDTO;
import com.example.estoque_api.dto.response.entity.ToolEntityResponseDTO;
import com.example.estoque_api.model.ToolEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class ToolEntityMapper {

    public ToolEntity toEntityTool(ToolEntityDTO dto) {
        return ToolEntity.builder()
                .name(dto.name())
                .active(dto.active())
                .build();
    }

    public ToolEntityResponseDTO toResponseEntityTool(ToolEntity entity) {
        return ToolEntityResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .active(entity.getActive())
                .createdAt(LocalDate.now())
                .build();
    }

    public void updateEntity(ToolEntity entity, ToolEntityDTO dto) {
        entity.setName(dto.name());
        entity.setActive(dto.active());
    }
}
