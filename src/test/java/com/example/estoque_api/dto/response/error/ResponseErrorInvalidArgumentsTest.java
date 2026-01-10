package com.example.estoque_api.dto.response.error;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseErrorInvalidArgumentsTest {

    private int statusCode;
    private LocalDate dateError;
    private List<ResponseErrorInvalidFields> invalidFields;

    @BeforeEach
    void setUp() {
        statusCode = 400;
        dateError = LocalDate.now();
        invalidFields = List.of(
                new ResponseErrorInvalidFields("toolName", "Name is required"),
                new ResponseErrorInvalidFields("cpf", "Invalid CPF format")
        );
    }

    @Test
    @DisplayName("Should successfully instantiate the record and return correct values")
    void shouldCreateResponseErrorInvalidArgumentsAndReturnCorrectValues() {
        var dto = new ResponseErrorInvalidArguments(statusCode, dateError, invalidFields);

        assertAll(
                () -> assertEquals(statusCode, dto.statusCode()),
                () -> assertEquals(dateError, dto.dateError()),
                () -> assertEquals(invalidFields, dto.invalidFields()),
                () -> assertEquals(2, dto.invalidFields().size())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void should_Verify_Equality() {
        var dto1 = new ResponseErrorInvalidArguments(statusCode, dateError, invalidFields);
        var dto2 = new ResponseErrorInvalidArguments(statusCode, dateError, invalidFields);
        var dto3 = new ResponseErrorInvalidArguments(500, dateError, List.of());

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void should_Verify_ToString() {
        var dto = new ResponseErrorInvalidArguments(statusCode, dateError, invalidFields);
        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("statusCode=" + statusCode)),
                () -> assertTrue(toString.contains("dateError=" + dateError)),
                () -> assertTrue(toString.contains("invalidFields=" + invalidFields))
        );
    }
}