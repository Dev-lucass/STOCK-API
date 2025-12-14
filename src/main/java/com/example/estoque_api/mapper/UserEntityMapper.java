package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.UserEntityDTO;
import com.example.estoque_api.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    UserEntity toEntity (UserEntityDTO dto);

    UserEntityDTO toDto (UserEntity entity);

}
