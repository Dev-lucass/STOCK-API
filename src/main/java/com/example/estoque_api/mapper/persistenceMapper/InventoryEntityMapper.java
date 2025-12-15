package com.example.estoque_api.mapper.persistenceMapper;

import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.model.InventoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    InventoryEntity toEntity(InventoryEntityDTO dto);

    @Mapping(target = "productId", source = "product.id")
    InventoryEntityDTO toDto(InventoryEntity inventory);
}
