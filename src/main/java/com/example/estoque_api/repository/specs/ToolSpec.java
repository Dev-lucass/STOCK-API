package com.example.estoque_api.repository.specs;

import com.example.estoque_api.model.ToolEntity;
import org.springframework.data.jpa.domain.Specification;

public class ToolSpec {

    public static Specification<ToolEntity> likeName (String name) {
        return (r, q, cb) -> cb.like(cb.lower(r.get("name")),  name.toLowerCase() + "%");
    }
}
