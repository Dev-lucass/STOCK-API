package com.example.estoque_api.dto.response.error;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseErrorReturnToInventoryTest {

    private int statusCode;
    private String message;
    private LocalDate dateError;

    @BeforeEach
    void setUp() {
        statusCode = 400;
        message = "Cannot return more items than were originally taken";
        dateError = LocalDate.now();
    }

    @Test
    @DisplayName("Should successfully instantiate the record and return correct values")
    void shouldCreateResponseErrorReturnToInventoryAndReturnCorrectValues() {
        ResponseErrorReturnToInventory dto = new ResponseErrorReturnToInventory(statusCode, message, dateError);

        assertAll(
                () -> assertEquals(statusCode, dto.statusCode()),
                () -> assertEquals(message, dto.message()),
                () -> assertEquals(dateError, dto.dateError())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        ResponseErrorReturnToInventory dto1 = new ResponseErrorReturnToInventory(statusCode, message, dateError);
        ResponseErrorReturnToInventory dto2 = new ResponseErrorReturnToInventory(statusCode, message, dateError);
        ResponseErrorReturnToInventory dto3 = new ResponseErrorReturnToInventory(409, "Different error", dateError);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        ResponseErrorReturnToInventory dto = new ResponseErrorReturnToInventory(statusCode, message, dateError);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("statusCode=" + statusCode)),
                () -> assertTrue(toString.contains("message=" + message)),
                () -> assertTrue(toString.contains("dateError=" + dateError))
        );
    }
}