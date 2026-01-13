package com.example.estoque_api.repository.custom;

import com.example.estoque_api.dto.request.filter.PeriodRequestDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.QHistoryEntity;
import com.example.estoque_api.model.QToolEntity;
import com.example.estoque_api.model.QUserEntity;
import com.example.estoque_api.repository.projection.MetricProjection;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HistoryRepositoryCustomImpl implements HistoryRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<MetricProjection> findTop10WithFilter(PeriodRequestDTO period) {

        var h = QHistoryEntity.historyEntity;
        var t = QToolEntity.toolEntity;
        var builder = new BooleanBuilder();

        builder.and(h.action.eq(InventoryAction.TAKE));

        if (period.startDate() != null) {
            builder.and(h.createdAt.goe(period.startDate().atStartOfDay()));
        }
        if (period.endDate() != null) {
            builder.and(h.createdAt.loe(period.endDate().atTime(LocalTime.MAX)));
        }

        var basicMetrics = query.select(Projections.constructor(MetricProjection.class,
                        t.id, t.name, h.createdAt.min(), t.usageCount,
                        h.quantityTaken.sum().castToNum(Integer.class),
                        Expressions.asNumber(0L),
                        Expressions.asString("")))
                .from(h).join(h.tool, t)
                .where(builder)
                .groupBy(t.id, t.name, t.usageCount)
                .orderBy(h.quantityTaken.sum().desc(), h.createdAt.min().asc())
                .limit(10)
                .fetch();

        return basicMetrics.stream()
                .map(m -> completeWithTopUser(m, period))
                .toList();
    }

    private MetricProjection completeWithTopUser(MetricProjection metric, PeriodRequestDTO period) {

        var h2 = QHistoryEntity.historyEntity;
        var u = QUserEntity.userEntity;
        var builder = new BooleanBuilder();

        builder.and(h2.tool.id.eq(metric.toolId()));
        builder.and(h2.action.eq(InventoryAction.TAKE));

        if (period.startDate() != null) {
            builder.and(h2.createdAt.goe(period.startDate().atStartOfDay()));
        }
        if (period.endDate() != null) {
            builder.and(h2.createdAt.loe(period.endDate().atTime(LocalTime.MAX)));
        }

        var topUser = query
                .select(u.id, u.username)
                .from(h2).join(h2.user, u)
                .where(builder)
                .groupBy(u.id, u.username)
                .orderBy(h2.quantityTaken.sum().desc(), h2.createdAt.min().asc())
                .limit(1).fetchOne();

        if (topUser == null) return metric;

        return new MetricProjection(
                metric.toolId(),
                metric.toolName(),
                metric.firstUsage(),
                metric.usageCount(),
                metric.totalTaked(),
                topUser.get(u.id),
                topUser.get(u.username)
        );
    }
}