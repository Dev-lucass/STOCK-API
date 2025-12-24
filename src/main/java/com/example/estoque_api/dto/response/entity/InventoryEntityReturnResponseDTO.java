package com.example.estoque_api.dto.response.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record InventoryEntityReturnResponseDTO(Long id,
                                                                                            String inventoryId,
                                                                                            Long idTool,
                                                                                            int quantityReturned,
                                                                                            int quantityInitial,
                                                                                            int quantityCurrent,
                                                                                            Double currentLifeCycle,
                                                                                            int usageCount,
                                                                                            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
                                                                                            LocalTime usageTime,
                                                                                            @DateTimeFormat(pattern = "dd/MM/yyyy")
                                                                                            LocalDate createdAt) {}
