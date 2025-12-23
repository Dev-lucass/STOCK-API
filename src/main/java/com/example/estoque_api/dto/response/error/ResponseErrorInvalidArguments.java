package com.example.estoque_api.dto.response.error;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;

public record ResponseErrorInvalidArguments(int statusCode,
                                                                                    @DateTimeFormat(pattern = "dd/MM/yyyy")
                                                                                    LocalDate dateError,
                                                                                     List<ResponseErrorInvalidFields> invalidFields){}
