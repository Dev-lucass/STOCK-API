package com.example.estoque_api.dto.response;

public record ResponseErrorInvalidFields(
        String defaultMessage,
        String field
)
{}
