package com.example.estoque_api.repository;

import com.example.estoque_api.model.QToolEntity;
import com.example.estoque_api.model.ToolEntity;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface ToolRepository extends JpaRepository<ToolEntity, Long>, QuerydslPredicateExecutor<ToolEntity>, QuerydslBinderCustomizer<QToolEntity> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByIdAndActiveFalse(Long id);

    @Override
    default void customize(QuerydslBindings bindings, QToolEntity tool){
        bindings.bind(tool.name).first(StringExpression::containsIgnoreCase);
    }
}
