package com.example.estoque_api.dto.response;

import java.time.LocalDateTime;

public record ProductEntityResponseDTO(
        String name,
        Boolean active,
        LocalDateTime createdAt
)

{}
