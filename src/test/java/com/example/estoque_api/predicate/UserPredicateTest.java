package com.example.estoque_api.predicate;

import com.example.estoque_api.dto.request.filter.UserFilterDTO;
import com.example.estoque_api.model.QUserEntity;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPredicateTest {

    @Test
    void build_WithNullFilter_ReturnsEmptyPredicate() {
        var result = UserPredicate.build(null);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(((BooleanBuilder) result).hasValue())
        );
    }

    @Test
    void build_WithFullFilter_ReturnsCompletePredicate() {
        var filter = UserFilterDTO.builder()
                .username("alice")
                .cpf("12345678901")
                .userActive(true)
                .build();

        var result = UserPredicate.build(filter);
        var predicateString = result.toString();

        assertAll(
                () -> assertTrue(predicateString.contains("startsWithIgnoreCase(userEntity.username,alice)")),
                () -> assertTrue(predicateString.contains("userEntity.cpf = 12345678901")),
                () -> assertTrue(predicateString.contains("userEntity.active = true"))
        );
    }

    @Test
    void build_WithBlankFields_ShouldNotAddPredicates() {
        var filter = UserFilterDTO.builder()
                .username("  ")
                .cpf("")
                .build();

        var result = UserPredicate.build(filter);

        assertFalse(((BooleanBuilder) result).hasValue());
    }

    @Test
    void build_WithCustomQEntity_UsesCorrectPath() {
        var filter = UserFilterDTO.builder().username("bob").build();
        var customQUser = new QUserEntity("customUser");

        var result = UserPredicate.build(filter, customQUser);

        assertTrue(result.toString().contains("customUser.username"));
    }

    @Test
    void build_WithOnlyUserActive_ReturnsActivePredicate() {
        var filter = UserFilterDTO.builder()
                .userActive(false)
                .build();

        var result = UserPredicate.build(filter);

        assertTrue(result.toString().contains("userEntity.active = false"));
    }
}