package com.example.estoque_api.repository.specs;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

public class HistoryEntitySpec {

    public static Specification<HistoryEntity> likeUser(String username) {
        return (r, q, cb) -> cb.like(cb.lower(r.get("user").get("username")), username.toLowerCase() + "%");
    }

    public static Specification<HistoryEntity> hasProduct(ProductEntity product) {
        return (r, q, cb) -> cb.equal(r.get("product"), product);
    }

    public static Specification<HistoryEntity> likeProductName(String productName) {
        return (r, q, cb) ->
                cb.like(cb.lower(r.get("product").get("name")), "%" + productName.toLowerCase() + "%");
    }

    public static Specification<HistoryEntity> hasAction(InventoryAction action) {
        return (r, q, cb) -> cb.equal(r.get("action"), action);
    }

    public static Specification<HistoryEntity> equalsQuantity(int quantity) {
        return (r, q, cb) -> cb.equal(r.get("quantity"), quantity);
    }

    public static Specification<HistoryEntity> minQuantity(int minQuantity) {
        return (r, q, cb) -> cb.greaterThanOrEqualTo(r.get("quantity"), minQuantity);
    }

    public static Specification<HistoryEntity> maxQuantity(int maxQuantity) {
        return (r, q, cb) -> cb.lessThanOrEqualTo(r.get("quantity"), maxQuantity);
    }
}

