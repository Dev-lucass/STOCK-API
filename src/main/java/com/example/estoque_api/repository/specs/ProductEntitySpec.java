package com.example.estoque_api.repository.specs;

import com.example.estoque_api.model.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

public class ProductEntitySpec {

    public static Specification<ProductEntity> likeName (String name) {
        return (r, q, cb) -> cb.like(cb.lower(r.get("name")), "%" + name.toLowerCase() + "%");
    }
}
