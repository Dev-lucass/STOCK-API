package com.example.estoque_api.repository;

import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.QInventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long>, QuerydslPredicateExecutor<InventoryEntity>, QuerydslBinderCustomizer<QInventoryEntity> {
    boolean existsByTool(ToolEntity tool);
    boolean existsByToolAndIdNot(ToolEntity tool, Long id);

    @Override
    default void customize(QuerydslBindings bindings, QInventoryEntity inventory){
        bindings.bind(inventory.tool.name).first(StringExpression::containsIgnoreCase);
    }
}
