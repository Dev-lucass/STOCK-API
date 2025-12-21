package com.example.estoque_api.dto.response;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public record ResponseErrorResouceNotFound(int StatusCode,
                                                                                       String message,
                                                                                       @DateTimeFormat(pattern = "dd/MM/yyyy")
                                                                                       LocalDate dateError) {}
