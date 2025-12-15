package com.example.estoque_api.mapper.responseMapper;

import com.example.estoque_api.dto.response.UserEntityResponseDTO;
import com.example.estoque_api.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserEntityResponseMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserEntityResponseDTO toResponse (UserEntity entity);

    List<UserEntityResponseDTO> toResponseList (List<UserEntity> userEntityList);
}
