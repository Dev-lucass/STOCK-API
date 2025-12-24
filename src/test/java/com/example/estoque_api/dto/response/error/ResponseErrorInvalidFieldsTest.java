package com.example.estoque_api.dto.response.error;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseErrorInvalidFieldsTest {

    private String message;
    private String field;

    @BeforeEach
    void setUp() {
        message = "Field cannot be null";
        field = "username";
    }

    @Test
    @DisplayName("Should successfully instantiate the record using builder and return correct values")
    void shouldCreateResponseErrorInvalidFieldsUsingBuilder() {
        ResponseErrorInvalidFields dto = ResponseErrorInvalidFields.builder()
                .message(message)
                .field(field)
                .build();

        assertAll(
                () -> assertEquals(message, dto.message()),
                () -> assertEquals(field, dto.field())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        ResponseErrorInvalidFields dto1 = new ResponseErrorInvalidFields(message, field);
        ResponseErrorInvalidFields dto2 = new ResponseErrorInvalidFields(message, field);
        ResponseErrorInvalidFields dto3 = new ResponseErrorInvalidFields("Invalid format", "email");

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        ResponseErrorInvalidFields dto = new ResponseErrorInvalidFields(message, field);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("message=" + message)),
                () -> assertTrue(toString.contains("field=" + field))
        );
    }
}