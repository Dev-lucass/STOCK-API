package com.example.estoque_api.dto.request.filter;

import lombok.Builder;

@Builder
public record UserFilterDTO(String username,
                            String cpf,
                            Boolean userActive) {}
