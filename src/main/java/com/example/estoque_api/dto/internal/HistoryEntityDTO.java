package com.example.estoque_api.dto.internal;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;

public record HistoryEntityDTO(UserEntity user,
                                                          ProductEntity product,
                                                          InventoryAction action,
                                                          int quantity) {}
