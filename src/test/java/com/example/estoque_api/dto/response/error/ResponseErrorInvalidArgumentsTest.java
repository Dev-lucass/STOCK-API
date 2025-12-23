package com.example.estoque_api.dto.response.error;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ResponseErrorInvalidArgumentsTest {

    @Test
    void should_create_response_error_invalid_arguments() {
        var date = LocalDate.now();

        var field = new ResponseErrorInvalidFields(
                "must not be blank",
                "must not be blank"
        );

        var dto = new ResponseErrorInvalidArguments(
                400,
                date,
                List.of(field)
        );

        assertEquals(400, dto.statusCode());
        assertEquals(date, dto.dateError());
        assertEquals(1, dto.invalidFields().size());
        assertEquals("must not be blank", dto.invalidFields().getFirst().field());
    }
}
