package com.example.estoque_api.dto.request;

public record TakeFromInventory(long userId,
                                long inventoryId,
                                int quantityTaken) {}
