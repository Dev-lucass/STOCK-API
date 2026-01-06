package com.example.estoque_api.dto.response.error;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseErrorConflictValueTest {

    private int statusCode;
    private String message;
    private LocalDateTime dateError;

    @BeforeEach
    void setUp() {
        statusCode = 409;
        message = "Conflict: Resource already exists";
        dateError = LocalDateTime.now();
    }

    @Test
    @DisplayName("Should successfully instantiate the record and return correct values")
    void shouldCreateResponseErrorConflictValueAndReturnCorrectValues() {
        ResponseErrorConflictValue dto = new ResponseErrorConflictValue(statusCode, message, dateError);

        assertAll(
                () -> assertEquals(statusCode, dto.statusCode()),
                () -> assertEquals(message, dto.message()),
                () -> assertEquals(dateError, dto.dateError())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        ResponseErrorConflictValue dto1 = new ResponseErrorConflictValue(statusCode, message, dateError);
        ResponseErrorConflictValue dto2 = new ResponseErrorConflictValue(statusCode, message, dateError);
        ResponseErrorConflictValue dto3 = new ResponseErrorConflictValue(400, "Bad Request", dateError);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        ResponseErrorConflictValue dto = new ResponseErrorConflictValue(statusCode, message, dateError);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("statusCode=" + statusCode)),
                () -> assertTrue(toString.contains("message=" + message)),
                () -> assertTrue(toString.contains("dateError=" + dateError))
        );
    }
}