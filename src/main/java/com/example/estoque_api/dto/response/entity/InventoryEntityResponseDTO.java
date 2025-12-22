package com.example.estoque_api.dto.response.entity;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Builder
public record InventoryEntityResponseDTO(Long id,
                                                                                int quantity,
                                                                                Long productId,
                                                                               @DateTimeFormat(pattern = "dd/MM/yyyy")
                                                                                LocalDate createdAt) {}
