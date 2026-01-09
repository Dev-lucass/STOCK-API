package com.example.estoque_api.dto.response.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;

public record ResponseErrorInvalidArguments( int statusCode,
                                             @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                             LocalDate dateError,
                                             List<ResponseErrorInvalidFields> invalidFields){}
