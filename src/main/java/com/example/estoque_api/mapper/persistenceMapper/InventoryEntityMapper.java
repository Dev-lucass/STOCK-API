package com.example.estoque_api.mapper.persistenceMapper;

import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.model.InventoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryEntityMapper {

    InventoryEntity toEntity (InventoryEntityDTO dto);

    InventoryEntityDTO toDto (InventoryEntity inventory);

}

