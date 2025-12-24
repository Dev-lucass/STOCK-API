package com.example.estoque_api.dto.response.error;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseErrorResouceNotFoundTest {

    private int statusCode;
    private String message;
    private LocalDate dateError;

    @BeforeEach
    void setUp() {
        statusCode = 404;
        message = "Resource not found with the provided ID";
        dateError = LocalDate.now();
    }

    @Test
    @DisplayName("Should successfully instantiate the record and return correct values")
    void shouldCreateResponseErrorResouceNotFoundAndReturnCorrectValues() {
        ResponseErrorResouceNotFound dto = new ResponseErrorResouceNotFound(statusCode, message, dateError);

        assertAll(
                () -> assertEquals(statusCode, dto.statusCode()),
                () -> assertEquals(message, dto.message()),
                () -> assertEquals(dateError, dto.dateError())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        ResponseErrorResouceNotFound dto1 = new ResponseErrorResouceNotFound(statusCode, message, dateError);
        ResponseErrorResouceNotFound dto2 = new ResponseErrorResouceNotFound(statusCode, message, dateError);
        ResponseErrorResouceNotFound dto3 = new ResponseErrorResouceNotFound(404, "Different message", dateError);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        ResponseErrorResouceNotFound dto = new ResponseErrorResouceNotFound(statusCode, message, dateError);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("statusCode=" + statusCode)),
                () -> assertTrue(toString.contains("message=" + message)),
                () -> assertTrue(toString.contains("dateError=" + dateError))
        );
    }
}