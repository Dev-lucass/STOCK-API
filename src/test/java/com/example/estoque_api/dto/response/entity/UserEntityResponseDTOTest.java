package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserEntityResponseDTOTest {

    private Long id;
    private String username;
    private LocalDate createdAt;

    @BeforeEach
    void setUp() {
        id = 1L;
        username = "alex_smith";
        createdAt = LocalDate.now();
    }

    @Test
    @DisplayName("Should successfully instantiate the record using builder and return correct values")
    void shouldCreateUserEntityResponseDTOUsingBuilder() {
        UserEntityResponseDTO dto = UserEntityResponseDTO.builder()
                .id(id)
                .username(username)
                .createdAt(createdAt)
                .build();

        assertAll(
                () -> assertEquals(id, dto.id()),
                () -> assertEquals(username, dto.username()),
                () -> assertEquals(createdAt, dto.createdAt())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        UserEntityResponseDTO dto1 = new UserEntityResponseDTO(id, username, createdAt);
        UserEntityResponseDTO dto2 = new UserEntityResponseDTO(id, username, createdAt);
        UserEntityResponseDTO dto3 = new UserEntityResponseDTO(2L, "other_user", createdAt);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        UserEntityResponseDTO dto = new UserEntityResponseDTO(id, username, createdAt);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("id=" + id)),
                () -> assertTrue(toString.contains("username=" + username)),
                () -> assertTrue(toString.contains("createdAt=" + createdAt))
        );
    }
}