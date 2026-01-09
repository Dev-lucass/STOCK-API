package com.example.estoque_api.dto.response.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ResponseErrorReturnToInventory(int statusCode,
                                             String message,
                                             @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                             LocalDateTime dateError) {}
