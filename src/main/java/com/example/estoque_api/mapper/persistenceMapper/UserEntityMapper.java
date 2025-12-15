package com.example.estoque_api.mapper.persistenceMapper;

import com.example.estoque_api.dto.request.UserEntityDTO;
import com.example.estoque_api.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(UserEntityDTO dto);

    UserEntityDTO toDto(UserEntity entity);
}
