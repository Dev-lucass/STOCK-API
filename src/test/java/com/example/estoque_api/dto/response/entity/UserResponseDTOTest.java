package com.example.estoque_api.dto.response.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserResponseDTOTest {

    private Long id;
    private String username;
    private LocalDateTime createdAt;

    @BeforeEach
    void setUp() {
        id = 1L;
        username = "alex_smith";
        createdAt = LocalDateTime.now();
    }

    @Test
    @DisplayName("Should successfully instantiate the record using builder and return correct values")
    void shouldCreateUserEntityResponseDTOUsingBuilder() {
        var dto = UserResponseDTO.builder()
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
    void should_Verify_Equality() {
        var dto1 = new UserResponseDTO(id, username, createdAt);
        var dto2 = new UserResponseDTO(id, username, createdAt);
        var dto3 = new UserResponseDTO(2L, "other_user", createdAt);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void should_Verify_ToString() {
        var dto = new UserResponseDTO(id, username, createdAt);
        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("id=" + id)),
                () -> assertTrue(toString.contains("username=" + username)),
                () -> assertTrue(toString.contains("createdAt=" + createdAt))
        );
    }
}