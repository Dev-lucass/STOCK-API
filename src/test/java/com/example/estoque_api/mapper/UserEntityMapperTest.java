package com.example.estoque_api.mapper;

import com.example.estoque_api.dto.request.UserEntityDTO;
import com.example.estoque_api.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserEntityMapperTest {

    private UserEntityMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new UserEntityMapper();
    }

    @Test
    void should_map_dto_to_entity() {
        var dto = new UserEntityDTO(
                "Lucas Silva",
                "11144477735",
                "Rua das Flores, 123"
        );

        var entity = mapper.toEntityUser(dto);

        assertEquals("Lucas Silva", entity.getUsername());
        assertEquals("11144477735", entity.getCpf());
        assertEquals("Rua das Flores, 123", entity.getAddress());
        assertNull(entity.getId());
    }

    @Test
    void should_map_entity_to_response_dto() {
        var entity = UserEntity.builder()
                .id(1L)
                .username("Lucas Silva")
                .build();

        var response = mapper.toResponseEntityUser(entity);

        assertEquals(1L, response.id());
        assertEquals("Lucas Silva", response.username());
        assertEquals(LocalDate.now(), response.createdAt());
    }

    @Test
    void should_update_entity_from_dto() {
        var entity = UserEntity.builder()
                .username("Nome Antigo")
                .cpf("00000000000")
                .address("Endereco Antigo")
                .build();

        var dto = new UserEntityDTO(
                "Nome Novo",
                "11144477735",
                "Endereco Novo"
        );

        mapper.updateEntity(entity, dto);

        assertEquals("Nome Novo", entity.getUsername());
        assertEquals("11144477735", entity.getCpf());
        assertEquals("Endereco Novo", entity.getAddress());
    }
}
