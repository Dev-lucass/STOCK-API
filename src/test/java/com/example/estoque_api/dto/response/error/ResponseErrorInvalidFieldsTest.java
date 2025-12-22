package com.example.estoque_api.dto.response.error;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResponseErrorInvalidFieldsTest {

    @Test
    void should_create_response_error_invalid_fields() {
        var dto = new ResponseErrorInvalidFields(
                "must not be blank",
                "username"
        );

        assertEquals("must not be blank", dto.message());
        assertEquals("username", dto.field());
    }

    @Test
    void should_create_response_error_invalid_fields_with_builder() {
        var dto = ResponseErrorInvalidFields.builder()
                .message("invalid size")
                .field("address")
                .build();

        assertEquals("invalid size", dto.message());
        assertEquals("address", dto.field());
    }
}
