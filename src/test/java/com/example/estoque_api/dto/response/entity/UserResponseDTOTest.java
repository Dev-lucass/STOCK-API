package com.example.estoque_api.dto.response.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserResponseDTOTest {

    @Test
    void builder_ShouldCreateDtoWithCorrectValues() {
        var now = LocalDateTime.now();

        var dto = UserResponseDTO.builder()
                .id(1L)
                .username("johndoe")
                .createdAt(now)
                .build();

        assertEquals(1L, dto.id());
        assertEquals("johndoe", dto.username());
        assertEquals(now, dto.createdAt());
    }

    @Test
    void jsonFormat_ShouldSerializeWithPattern() throws Exception {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        var date = LocalDateTime.of(2026, 1, 10, 19, 30, 0);
        var dto = UserResponseDTO.builder()
                .id(1L)
                .username("johndoe")
                .createdAt(date)
                .build();

        var json = mapper.writeValueAsString(dto);
        var expectedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        assertTrue(json.contains(expectedDate));
    }
}