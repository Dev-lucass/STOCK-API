package com.example.estoque_api.repository.specs;

import com.example.estoque_api.model.InventoryEntity;
import org.springframework.data.jpa.domain.Specification;

public class InventoryEntitySpec {

    public static Specification<InventoryEntity> equalsQuantity(int quantity) {
        return (r, q, cb) -> cb.equal(r.get("quantity"), quantity);
    }

    public static Specification<InventoryEntity> likeProductName(String productName) {
        return (r, q, cb) -> cb.like(cb.lower(r.get("product").get("name")), "%" + productName.toLowerCase() + "%");
    }
}
