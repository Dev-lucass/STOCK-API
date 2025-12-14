package com.example.estoque_api.dto.response;

import java.util.List;

public record ResponseErrorInvalidArguments(
        int httpStatus,
        String message,
        List<ResponseErrorInvalidFields> invalidFields
)

{}
