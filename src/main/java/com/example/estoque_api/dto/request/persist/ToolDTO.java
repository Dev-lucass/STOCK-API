package com.example.estoque_api.dto.request.persist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ToolDTO(@NotBlank @Size(min = 2, max = 255) String name,
                      @NotNull boolean active) {}
