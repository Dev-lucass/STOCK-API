package com.example.estoque_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ToolDTO(@NotBlank @Size(min = 2, max = 255) String name,
                                          @NotNull Boolean active) {}
