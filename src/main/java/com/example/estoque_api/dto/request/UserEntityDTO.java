package com.example.estoque_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record UserEntityDTO (@NotBlank @Size(min = 2, max = 120) String username,
                                                      @NotBlank @CPF String cpf,
                                                      @NotBlank @Size(min = 5, max = 255) String address) {}
