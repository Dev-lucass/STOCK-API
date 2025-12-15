package com.example.estoque_api.mapper.responseMapper;

import com.example.estoque_api.dto.response.InventoryEntityResponseDTO;
import com.example.estoque_api.model.InventoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryEntityResponseMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "productId", expression = "java(entity.getProduct().getId())")
    InventoryEntityResponseDTO toResponse(InventoryEntity entity);

    List<InventoryEntityResponseDTO> toResponseList(List<InventoryEntity> inventoryEntityList);
}
