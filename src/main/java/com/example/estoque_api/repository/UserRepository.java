package com.example.estoque_api.repository;

import com.example.estoque_api.model.QUserEntity;
import com.example.estoque_api.model.UserEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface UserRepository extends JpaRepository<UserEntity, Long>, QuerydslPredicateExecutor<UserEntity>, QuerydslBinderCustomizer<QUserEntity> {
    Boolean existsByCpf(String cpf);
    Boolean existsByCpfAndIdNot(String cpf, Long id);

    @Override
    default void customize(QuerydslBindings bindings, QUserEntity user){
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.bind(user.cpf).first(StringExpression::eq);
        bindings.bind(user.active).first(BooleanExpression::eq);
    }
}
