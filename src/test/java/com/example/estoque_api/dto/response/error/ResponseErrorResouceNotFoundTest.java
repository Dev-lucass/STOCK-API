package com.example.estoque_api.dto.response.error;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ResponseErrorResouceNotFoundTest {

    @Test
    void should_create_response_error_resource_not_found() {
        var date = LocalDate.now();

        var dto = new ResponseErrorResouceNotFound(
                404,
                "Resource not found",
                date
        );

        assertEquals(404, dto.StatusCode());
        assertEquals("Resource not found", dto.message());
        assertEquals(date, dto.dateError());
    }
}
