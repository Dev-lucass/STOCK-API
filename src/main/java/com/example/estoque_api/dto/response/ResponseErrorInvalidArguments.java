package com.example.estoque_api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseErrorInvalidArguments(
        int httpStatus,
        String message,
        LocalDateTime dateTimeError,
        List<ResponseErrorInvalidFields> invalidFields
)

{}
