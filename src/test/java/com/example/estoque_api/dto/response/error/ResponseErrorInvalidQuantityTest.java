package com.example.estoque_api.dto.response.error;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ResponseErrorInvalidQuantityTest {

    @Test
    void should_create_response_error_invalid_quantity() {
        var date = LocalDate.now();

        var dto = new ResponseErrorInvalidQuantity(
                400,
                "Invalid quantity",
                date
        );

        assertEquals(400, dto.statusCode());
        assertEquals("Invalid quantity", dto.message());
        assertEquals(date, dto.dateError());
    }
}
