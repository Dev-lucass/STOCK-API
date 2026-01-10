package com.example.estoque_api.predicate;

import com.example.estoque_api.dto.request.filter.UserFilterDTO;
import com.example.estoque_api.model.QUserEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.util.Optional;

public class UserPredicate {

    public static Predicate build(UserFilterDTO filter) {
        return build(filter, QUserEntity.userEntity);
    }

    public static BooleanBuilder build(UserFilterDTO filter, QUserEntity qUser) {
        var builder = new BooleanBuilder();

        if (filter == null) return builder;

        Optional.ofNullable(filter.username())
                .filter(s -> !s.isBlank())
                .ifPresent(v -> builder.and(qUser.username.startsWithIgnoreCase(v)));

        Optional.ofNullable(filter.cpf())
                .filter(s -> !s.isBlank())
                .ifPresent(v -> builder.and(qUser.cpf.eq(v)));

        Optional.ofNullable(filter.userActive())
                .ifPresent(v -> builder.and(qUser.active.eq(v)));

        return builder;
    }
}