package com.example.estoque_api.predicate;

import com.example.estoque_api.dto.request.filter.ToolFilterDTO;
import com.example.estoque_api.model.QToolEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import java.time.LocalTime;
import java.util.Optional;

public class ToolPredicate {

    public static Predicate build(ToolFilterDTO filter) {
        return build(filter, QToolEntity.toolEntity);
    }

    public static BooleanBuilder build(ToolFilterDTO filter, QToolEntity qTool) {
        var builder = new BooleanBuilder();

        if (filter == null) return builder;

        Optional.ofNullable(filter.toolName())
                .filter(s -> !s.isBlank())
                .ifPresent(s -> builder.and(qTool.name.startsWithIgnoreCase(s)));

        Optional.ofNullable(filter.toolActive())
                .ifPresent(b -> builder.and(qTool.active.eq(b)));

        Optional.ofNullable(filter.inUse())
                .ifPresent(b -> builder.and(qTool.inUse.eq(b)));

        Optional.ofNullable(filter.usageCount())
                .ifPresent(i -> builder.and(qTool.usageCount.eq(i)));

        var timeUsage = buildTimeUsage(filter);

        if (!timeUsage.equals(LocalTime.MIDNIGHT)) {
            builder.and(qTool.usageTime.goe(timeUsage));
        }

        return builder;
    }

    private static LocalTime buildTimeUsage(ToolFilterDTO filter) {
        int h = filter.hourUsage() != null ? filter.hourUsage() : 0;
        int m = filter.minutesUsage() != null ? filter.minutesUsage() : 0;
        int s = filter.secondsUsage() != null ? filter.secondsUsage() : 0;
        return LocalTime.of(h, m, s);
    }
}