package com.example.estoque_api.mapper.responseMapper;

import com.example.estoque_api.dto.response.ProductEntityResponseDTO;
import com.example.estoque_api.model.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductEntityResponseMapper {

    ProductEntityResponseDTO toResponse(ProductEntity entity);

    List<ProductEntityResponseDTO> toResponseList(List<ProductEntity> productEntityList);
}
