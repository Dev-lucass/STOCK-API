package com.example.estoque_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductEntityDTO(

        @NotBlank(message = "Require field name")
        @Size(message = "Size field: min = 2 and max = 255", min = 2, max = 255)
        String name,

        @NotNull(message = "Require field active")
        Boolean active
) {
}
