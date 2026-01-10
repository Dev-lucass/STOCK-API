package com.example.estoque_api.repository;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.QHistoryEntity;
import com.example.estoque_api.model.UserEntity;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.List;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Long>, QuerydslPredicateExecutor<HistoryEntity>, QuerydslBinderCustomizer<QHistoryEntity> {
    boolean existsByUserAndAction(UserEntity user, InventoryAction action);
    List<HistoryEntity> findByUser(UserEntity user);

    @Override
    default void customize(QuerydslBindings bindings, QHistoryEntity history) {
        bindings.bind(String.class).first((StringPath path, String v) -> path.containsIgnoreCase(v));
    }
}
