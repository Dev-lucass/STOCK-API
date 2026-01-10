package com.example.estoque_api.dto.request.persist;

public record TakeFromInventory(long userId,
                                long inventoryId,
                                int quantityTaken) {}
