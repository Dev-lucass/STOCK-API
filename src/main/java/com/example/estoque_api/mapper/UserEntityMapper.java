package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.UserEntityDTO;
import com.example.estoque_api.dto.response.entity.UserEntityResponseDTO;
import com.example.estoque_api.model.UserEntity;
import java.time.LocalDate;

public class UserEntityMapper {

    public UserEntity toEntityUser (UserEntityDTO dto) {
        return UserEntity.builder()
                .username(dto.username())
                .cpf(dto.cpf())
                .address(dto.address())
                .build();
    }

    public UserEntityResponseDTO toResponseEntityUser (UserEntity entity) {
        return UserEntityResponseDTO.builder()
                .username(entity.getUsername())
                .createdAt(LocalDate.now())
                .build();
    }

    public void updateEntity(UserEntity entity, UserEntityDTO dto) {
        entity.setUsername(dto.username());
        entity.setCpf(dto.cpf());
        entity.setAddress(dto.address());
    }
}
