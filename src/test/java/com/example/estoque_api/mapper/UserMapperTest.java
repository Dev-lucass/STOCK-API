package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.persist.UserDTO;
import com.example.estoque_api.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperTest {

    private UserMapper mapper;
    private UserDTO userDTO;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        mapper = new UserMapper();

        userDTO = new UserDTO("john_doe", "123.456.789-00", "Main St, 123");

        userEntity = UserEntity.builder()
                .id(1L)
                .username("jane_doe")
                .cpf("000.000.000-00")
                .address("Old St, 456")
                .build();
    }

    @Test
    @DisplayName("Should map UserDTO to UserEntity successfully")
    void shouldMapDtoToEntityUser() {
        var result = mapper.toEntityUser(userDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userDTO.username(), result.getUsername()),
                () -> assertEquals(userDTO.cpf(), result.getCpf()),
                () -> assertEquals(userDTO.address(), result.getAddress())
        );
    }

    @Test
    @DisplayName("Should map UserEntity to UserResponseDTO successfully")
    void shouldMapEntityToResponseEntityUser() {
        var result = mapper
                .toResponseEntityUser(userEntity);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userEntity.getId(), result.id()),
                () -> assertEquals(userEntity.getUsername(), result.username())
        );

        assertThat(result.createdAt()).isCloseTo(LocalDateTime.now(), within(1, SECONDS));
    }

    @Test
    @DisplayName("Should update existing UserEntity with DTO data")
    void shouldUpdateEntity() {
        mapper.updateEntity(userEntity, userDTO);

        assertAll(
                () -> assertEquals(userDTO.username(), userEntity.getUsername()),
                () -> assertEquals(userDTO.cpf(), userEntity.getCpf()),
                () -> assertEquals(userDTO.address(), userEntity.getAddress()),
                () -> assertEquals(1L, userEntity.getId())
        );
    }
}