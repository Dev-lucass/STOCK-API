package com.example.estoque_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record UserEntityDTO (

            @NotBlank(message = "Require field username")
            @Size(message = "Size field: min = 2 and max = 120", min = 2, max = 120)
            String username,

            @NotBlank(message = "CPF is required")
            @Pattern(
                    regexp = "\\d{11}",
                    message = "CPF must contain exactly 11 digits"
            )
            @CPF(message = "Invalid CPF, try again")
            String cpf,

            @NotBlank(message = "Require field address")
            @Size(message = "Size field: min = 5 and max = 255", min = 5, max = 255)
            String address
)

{}
