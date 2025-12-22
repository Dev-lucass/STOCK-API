package com.example.estoque_api.dto.request;

public record TakeFromInventory(Long userId,
                                                            int quantity,
                                                            Long productId){}
