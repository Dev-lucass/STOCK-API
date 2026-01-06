package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.UserDTO;
import com.example.estoque_api.dto.response.entity.UserResponseDTO;
import com.example.estoque_api.model.UserEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class UserMapper {

    public UserEntity toEntityUser (UserDTO dto) {
        return UserEntity.builder()
                .username(dto.username())
                .cpf(dto.cpf())
                .address(dto.address())
                .build();
    }

    public UserResponseDTO toResponseEntityUser (UserEntity entity) {
        return UserResponseDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void updateEntity(UserEntity entity, UserDTO dto) {
        entity.setUsername(dto.username());
        entity.setCpf(dto.cpf());
        entity.setAddress(dto.address());
    }
}
