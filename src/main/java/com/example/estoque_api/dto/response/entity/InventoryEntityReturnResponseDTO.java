package com.example.estoque_api.dto.response.entity;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Builder
public record InventoryEntityReturnResponseDTO(Long id,
                                                                                            String inventoryId,
                                                                                            int quantityReturned,
                                                                                            int quantityInitial,
                                                                                            int quantityCurrent,
                                                                                            Long idTool,
                                                                                            @DateTimeFormat(pattern = "dd/MM/yyyy")
                                                                                            LocalDate createdAt) {}
