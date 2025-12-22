package com.example.estoque_api.dto.response.error;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ResponseErrorConflictValueTest {

    @Test
    void should_create_response_error_conflict_value() {
        var date = LocalDate.now();

        var dto = new ResponseErrorConflictValue(
                409,
                "CPF already exists",
                date
        );

        assertEquals(409, dto.StatusCode());
        assertEquals("CPF already exists", dto.message());
        assertEquals(date, dto.dateError());
    }
}
