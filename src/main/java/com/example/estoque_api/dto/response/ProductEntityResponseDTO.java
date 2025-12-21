package com.example.estoque_api.dto.response;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Builder
public record ProductEntityResponseDTO(Long id,
                                                                             String name,
                                                                             Boolean active,
                                                                             @DateTimeFormat(pattern = "dd/MM/yyyy")
                                                                             LocalDate createdAt) {}
