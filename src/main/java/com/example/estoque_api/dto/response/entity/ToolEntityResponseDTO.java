package com.example.estoque_api.dto.response.entity;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Builder
public record ToolEntityResponseDTO(Long id,
                                                                      String name,
                                                                      Boolean active,
                                                                      @DateTimeFormat(pattern = "dd/MM/yyyy")
                                                                      LocalDate createdAt) {}
