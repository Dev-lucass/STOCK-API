package com.example.estoque_api.dto.response.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record ToolResponseDTO(Long id,
                              String name,
                              Boolean active,
                              @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                              LocalDateTime createdAt) {}
