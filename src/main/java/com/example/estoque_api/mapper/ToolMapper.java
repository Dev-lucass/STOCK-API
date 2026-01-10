package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.persist.ToolDTO;
import com.example.estoque_api.dto.response.entity.ToolResponseDTO;
import com.example.estoque_api.dto.response.filter.ToolFilterResponseDTO;
import com.example.estoque_api.model.ToolEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class ToolMapper {

    public ToolEntity toEntityTool(ToolDTO dto) {
        return ToolEntity.builder()
                .name(dto.name())
                .active(dto.active())
                .createdIn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
    }

    public ToolResponseDTO toResponseEntityTool(ToolEntity entity) {
        return ToolResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .active(entity.getActive())
                .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
    }

    public void updateEntity(ToolEntity entity, ToolDTO dto) {
        entity.setName(dto.name());
        entity.setActive(dto.active());
    }

    public ToolFilterResponseDTO toFilterResponse(ToolEntity tool) {
        return ToolFilterResponseDTO.builder()
                .id(tool.getId())
                .name(tool.getName())
                .active(tool.getActive())
                .inUse(tool.getInUse())
                .usageCount(tool.getUsageCount())
                .usageTime(tool.getUsageTime())
                .createdAt(tool.getCreatedIn())
                .build();
    }
}
