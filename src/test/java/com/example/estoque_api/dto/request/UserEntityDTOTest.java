package com.example.estoque_api.dto.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserEntityDTOTest {

    @Test
    void should_create_user_entity_dto() {
        var dto = new UserEntityDTO(
                "Lucas Silva",
                "11144477735",
                "Rua das Flores, 123"
        );

        assertEquals("Lucas Silva", dto.username());
        assertEquals("11144477735", dto.cpf());
        assertEquals("Rua das Flores, 123", dto.address());
    }

    @Test
    void should_allow_any_values_when_not_validated() {
        var dto = new UserEntityDTO(
                "",
                "",
                ""
        );

        assertEquals("", dto.username());
        assertEquals("", dto.cpf());
        assertEquals("", dto.address());
    }
}
