package com.example.estoque_api.dto.response;

import lombok.Builder;

@Builder
public record ResponseErrorInvalidFields(String message,
                                                                            String field){}
