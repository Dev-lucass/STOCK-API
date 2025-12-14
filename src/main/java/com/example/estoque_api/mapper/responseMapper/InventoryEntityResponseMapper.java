package com.example.estoque_api.mapper.responseMapper;

import com.example.estoque_api.dto.response.InventoryEntityResponseDTO;
import com.example.estoque_api.model.InventoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryEntityResponseMapper {
    InventoryEntityResponseDTO toResponse (InventoryEntity entity);
    List<InventoryEntityResponseDTO> toResponseList (List<InventoryEntity> inventoryEntityList);
}
