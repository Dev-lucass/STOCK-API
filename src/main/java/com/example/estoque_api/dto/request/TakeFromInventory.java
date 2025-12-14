package com.example.estoque_api.dto.request;

import java.util.UUID;

public record TakeFromInventory(Long userId,
                                UUID inventoryId,
                                int quantityTaken) {}
