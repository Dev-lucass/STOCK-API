package com.example.estoque_api.mapper.responseMapper;

import com.example.estoque_api.dto.response.UserEntityResponseDTO;
import com.example.estoque_api.model.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserEntityResponseMapper {

    UserEntityResponseDTO toResponse (UserEntity entity);
    List<UserEntityResponseDTO> toResponseList (List<UserEntity> userEntityList);
}
