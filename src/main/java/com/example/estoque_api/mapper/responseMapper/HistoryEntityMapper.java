package com.example.estoque_api.mapper.responseMapper;

import com.example.estoque_api.dto.response.HistoryResponseDTO;
import com.example.estoque_api.model.HistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HistoryEntityMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "createdAt", source = "id.createdAt")
    HistoryResponseDTO toResponse(HistoryEntity entity);
}
