package com.example.estoque_api.dto.response.filter;

import lombok.Builder;

@Builder
public record UserFilterResponseDTO(long id,
                                    String username,
                                    String cpf,
                                    boolean active,
                                    String address){}
