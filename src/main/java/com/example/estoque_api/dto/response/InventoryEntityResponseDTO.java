package com.example.estoque_api.dto.response;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Builder
public record InventoryEntityResponseDTO(Long id,
                                                                                Integer quantity,
                                                                                Long productId,
                                                                               @DateTimeFormat(pattern = "dd/MM/yyyy")
                                                                                LocalDate createdAt) {}
