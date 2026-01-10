package com.example.estoque_api.dto.filter;

import com.example.estoque_api.dto.request.filter.UserFilterDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserFilterDTOTest {

    @Test
    void shouldBuildUserFilterDTOWithAllFields() {
        var username = "john.doe";
        var cpf = "12345678901";
        var userActive = true;

        var dto = UserFilterDTO.builder()
                .username(username)
                .cpf(cpf)
                .userActive(userActive)
                .build();

        assertAll(
                () -> assertEquals(username, dto.username()),
                () -> assertEquals(cpf, dto.cpf()),
                () -> assertEquals(userActive, dto.userActive())
        );
    }

    @Test
    void shouldVerifyRecordEqualityAndHashCode() {
        var dto1 = UserFilterDTO.builder()
                .username("test.user")
                .cpf("00011122233")
                .build();

        var dto2 = UserFilterDTO.builder()
                .username("test.user")
                .cpf("00011122233")
                .build();

        var dto3 = UserFilterDTO.builder()
                .username("other.user")
                .cpf("99988877766")
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
        var dto = UserFilterDTO.builder()
                .username("scanned.user")
                .userActive(false)
                .build();

        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("scanned.user")),
                () -> assertTrue(toString.contains("userActive=false"))
        );
    }
}