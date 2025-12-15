package com.example.estoque_api.mapper.responseMapper;

import com.example.estoque_api.dto.response.ProductEntityResponseDTO;
import com.example.estoque_api.model.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductEntityResponseMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    ProductEntityResponseDTO toResponse(ProductEntity entity);

    List<ProductEntityResponseDTO> toResponseList(List<ProductEntity> productEntityList);
}
