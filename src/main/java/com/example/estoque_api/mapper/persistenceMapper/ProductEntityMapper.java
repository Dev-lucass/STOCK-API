package com.example.estoque_api.mapper.persistenceMapper;

import com.example.estoque_api.dto.request.ProductEntityDTO;
import com.example.estoque_api.model.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {

    @Mapping(target = "id", ignore = true)
    ProductEntity toEntity(ProductEntityDTO dto);

    ProductEntityDTO toDto(ProductEntity product);
}
