package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UserEntityResponseDTOTest {

    @Test
    void should_create_user_entity_response_dto() {
        var date = LocalDate.now();

        var dto = UserEntityResponseDTO.builder()
                .id(1L)
                .username("Lucas Silva")
                .createdAt(date)
                .build();

        assertEquals(1L, dto.id());
        assertEquals("Lucas Silva", dto.username());
        assertEquals(date, dto.createdAt());
    }
}
