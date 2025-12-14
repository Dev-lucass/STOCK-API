package com.example.estoque_api.dto.response;

import java.time.LocalDateTime;

public record ProductEntityResponseDTO(
        String name,
        Boolean isActive,
        LocalDateTime createdAt
)

{}
