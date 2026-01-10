package com.example.estoque_api.dto.request.persist;

import lombok.Builder;

@Builder
public record TakeFromInventory(long userId,
                                long inventoryId,
                                int quantityTaken) {}
