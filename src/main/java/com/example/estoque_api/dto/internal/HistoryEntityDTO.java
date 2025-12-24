package com.example.estoque_api.dto.internal;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import lombok.Builder;

@Builder
public record HistoryEntityDTO(UserEntity user,
                                                           String inventoryId,
                                                           ProductEntity product,
                                                           InventoryAction action,
                                                           int quantityTaken) {}
