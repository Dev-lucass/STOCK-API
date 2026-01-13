package com.example.estoque_api.predicate;

import com.example.estoque_api.dto.request.filter.HistoryFilterDTO;
import com.example.estoque_api.dto.request.filter.ToolFilterDTO;
import com.example.estoque_api.dto.request.filter.UserFilterDTO;
import com.example.estoque_api.model.QHistoryEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.util.Optional;

public class HistoryPredicate {

    public static Predicate build(HistoryFilterDTO filter) {
        var builder = new BooleanBuilder();
        var qHistory = QHistoryEntity.historyEntity;

        if (filter == null) return builder;

        var userFilter = buildUserFilter(filter);
        var toolFilter = buildToolFilter(filter);

        builder.and(UserPredicate.build(userFilter, qHistory.user));
        builder.and(ToolPredicate.build(toolFilter, qHistory.tool));
        builder.and(InventoryPredicate.buildOnlyId(filter.inventoryId()));

        Optional.ofNullable(filter.action())
                .ifPresent(a -> builder.and(qHistory.action.eq(a)));

        return builder;
    }

    private static UserFilterDTO buildUserFilter(HistoryFilterDTO filter) {
        return UserFilterDTO.builder()
                .username(filter.username())
                .cpf(filter.cpf())
                .userActive(filter.userActive())
                .build();
    }

    private static ToolFilterDTO buildToolFilter(HistoryFilterDTO filter) {
        return ToolFilterDTO.builder()
                .toolName(filter.toolName())
                .toolActive(filter.toolActive())
                .inUse(filter.inUse())
                .usageCount(filter.usageCount())
                .build();
    }
}

