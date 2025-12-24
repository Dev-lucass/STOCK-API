package com.example.estoque_api.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserEntityDTOTest {

    @Test
    @DisplayName("Should successfully instantiate the record using builder and return correct values")
    void shouldCreateUserEntityDTOUsingBuilder() {
        String expectedUsername = "John Doe";
        String expectedCpf = "12345678901";
        String expectedAddress = "123 Main Street, NY";

        UserEntityDTO dto = UserEntityDTO.builder()
                .username(expectedUsername)
                .cpf(expectedCpf)
                .address(expectedAddress)
                .build();

        assertAll(
                () -> assertEquals(expectedUsername, dto.username()),
                () -> assertEquals(expectedCpf, dto.cpf()),
                () -> assertEquals(expectedAddress, dto.address())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        UserEntityDTO dto1 = new UserEntityDTO("User", "123", "Street A");
        UserEntityDTO dto2 = new UserEntityDTO("User", "123", "Street A");
        UserEntityDTO dto3 = new UserEntityDTO("Other", "456", "Street B");

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        UserEntityDTO dto = new UserEntityDTO("Jane Doe", "98765432100", "Avenue 5");
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("username=Jane Doe")),
                () -> assertTrue(toString.contains("cpf=98765432100")),
                () -> assertTrue(toString.contains("address=Avenue 5"))
        );
    }
}