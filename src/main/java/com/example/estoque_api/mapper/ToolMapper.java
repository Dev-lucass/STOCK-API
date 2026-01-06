package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.ToolDTO;
import com.example.estoque_api.dto.response.entity.ToolResponseDTO;
import com.example.estoque_api.model.ToolEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class ToolMapper {

    public ToolEntity toEntityTool(ToolDTO dto) {
        return ToolEntity.builder()
                .name(dto.name())
                .active(dto.active())
                .build();
    }

    public ToolResponseDTO toResponseEntityTool(ToolEntity entity) {
        return ToolResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .active(entity.getActive())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void updateEntity(ToolEntity entity, ToolDTO dto) {
        entity.setName(dto.name());
        entity.setActive(dto.active());
    }
}
