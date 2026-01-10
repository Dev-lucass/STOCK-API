package com.example.estoque_api.predicate;

import com.example.estoque_api.dto.request.filter.InventoryFilterDTO;
import com.example.estoque_api.model.QInventoryEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.util.Optional;

public class InventoryPredicate {

    private static final BooleanBuilder builder = new BooleanBuilder();
    private static final QInventoryEntity qInventory = QInventoryEntity.inventoryEntity;

    public static Predicate build(InventoryFilterDTO filter) {

        if (filter == null) return builder;

        Optional.ofNullable(filter.quantityInitial())
                .ifPresent(i -> builder.and(qInventory.quantityInitial.eq(i)));

        Optional.ofNullable(filter.quantityCurrent())
                .ifPresent(i -> builder.and(qInventory.quantityCurrent.eq(i)));

        Optional.ofNullable(filter.toolId())
                .ifPresent(i -> builder.and(qInventory.tool.id.eq(i)));

        Optional.ofNullable(filter.toolName())
                .filter(s -> !s.isBlank())
                .ifPresent(s -> builder.and(qInventory.tool.name.startsWithIgnoreCase(s)));

        return builder;
    }

    public static Predicate buildOnlyId(Long id) {
        Optional.ofNullable(id)
                .ifPresent(i -> builder.and(qInventory.tool.id.eq(i)));
        return builder;
    }
}
