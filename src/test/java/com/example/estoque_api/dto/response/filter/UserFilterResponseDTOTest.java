package com.example.estoque_api.dto.response.filter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserFilterResponseDTOTest {

    @Test
    void shouldBuildUserFilterResponseDTOWithAllFields() {
        var id = 1L;
        var username = "johndoe";
        var cpf = "12345678901";
        var active = true;
        var address = "123 Main St";

        var dto = UserFilterResponseDTO.builder()
                .id(id)
                .username(username)
                .cpf(cpf)
                .active(active)
                .address(address)
                .build();

        assertAll(
                () -> assertEquals(id, dto.id()),
                () -> assertEquals(username, dto.username()),
                () -> assertEquals(cpf, dto.cpf()),
                () -> assertTrue(dto.active()),
                () -> assertEquals(address, dto.address())
        );
    }

    @Test
    void shouldVerifyRecordEqualityAndHashCode() {
        var dto1 = UserFilterResponseDTO.builder()
                .id(1L)
                .username("testuser")
                .build();

        var dto2 = UserFilterResponseDTO.builder()
                .id(1L)
                .username("testuser")
                .build();

        var dto3 = UserFilterResponseDTO.builder()
                .id(2L)
                .username("otheruser")
                .build();

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3),
                () -> assertNotEquals(dto1.hashCode(), dto3.hashCode())
        );
    }

    @Test
    void shouldVerifyToStringContainsCorrectData() {
        var dto = UserFilterResponseDTO.builder()
                .id(10L)
                .username("scanned.user")
                .cpf("11122233344")
                .build();

        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("id=10")),
                () -> assertTrue(toString.contains("username=scanned.user")),
                () -> assertTrue(toString.contains("cpf=11122233344"))
        );
    }
}