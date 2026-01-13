package com.example.estoque_api.repository.custom;

import com.example.estoque_api.dto.request.filter.PeriodRequestDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.QHistoryEntity;
import com.example.estoque_api.model.QToolEntity;
import com.example.estoque_api.model.QUserEntity;
import com.example.estoque_api.repository.projection.MetricProjection;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HistoryRepositoryCustomImpl implements HistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MetricProjection> findTop10WithFilter(PeriodRequestDTO period) {

        var h = QHistoryEntity.historyEntity;
        var h2 = new QHistoryEntity("h2");
        var t = QToolEntity.toolEntity;
        var u = QUserEntity.userEntity;
        var builder = new BooleanBuilder();

        builder.and(h.action.eq(InventoryAction.TAKE));

        if (period.startDate() != null) {
            builder.and(h.createdAt.goe(period.startDate().atStartOfDay()));
        }
        if (period.endDate() != null) {
            builder.and(h.createdAt.loe(period.endDate().atTime(LocalTime.MAX)));
        }

        return queryFactory.select(Projections.constructor(MetricProjection.class,
                        t.id,
                        t.name,
                        h.createdAt.min(),
                        t.usageCount,
                        h.quantityTaken.sum().castToNum(Integer.class),

                        JPAExpressions.select(u.id)
                                .from(h2)
                                .join(h2.user, u)
                                .where(h2.tool.id.eq(t.id).and(h2.action.eq(InventoryAction.TAKE)))
                                .groupBy(u.id)
                                .orderBy(h2.quantityTaken.sum().desc())
                                .limit(1),

                        JPAExpressions.select(u.username)
                                .from(h2)
                                .join(h2.user, u)
                                .where(h2.tool.id.eq(t.id).and(h2.action.eq(InventoryAction.TAKE)))
                                .groupBy(u.id, u.username)
                                .orderBy(h2.quantityTaken.sum().desc())
                                .limit(1)
                ))
                .from(h)
                .join(h.tool, t)
                .where(builder)
                .groupBy(t.id, t.name, t.usageCount)
                .orderBy(h.quantityTaken.sum().desc(), h.createdAt.min().asc())
                .limit(10)
                .fetch();
    }
}