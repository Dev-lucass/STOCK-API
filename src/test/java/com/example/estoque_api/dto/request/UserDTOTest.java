package com.example.estoque_api.dto.request;

import com.example.estoque_api.dto.request.persist.UserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDTOTest {

    @Test
    @DisplayName("Should successfully instantiate the record using builder and return correct values")
    void shouldCreateUserEntityDTOUsingBuilder() {
        var expectedUsername = "John Doe";
        var expectedCpf = "12345678901";
        var expectedAddress = "123 Main Street, NY";

        var dto = UserDTO.builder()
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
        var dto1 = new UserDTO("User", "123", "Street A");
        var dto2 = new UserDTO("User", "123", "Street A");
        var dto3 = new UserDTO("Other", "456", "Street B");

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        var dto = new UserDTO("Jane Doe", "98765432100", "Avenue 5");
        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("username=Jane Doe")),
                () -> assertTrue(toString.contains("cpf=98765432100")),
                () -> assertTrue(toString.contains("address=Avenue 5"))
        );
    }
}