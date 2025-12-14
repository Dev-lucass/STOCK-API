package com.example.estoque_api.dto.response;

import java.time.LocalDateTime;

public record UserEntityResponseDTO(
        String username,
        String address,
        LocalDateTime createdAt
)
{}
