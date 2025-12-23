package com.example.estoque_api.repository.specs;

import com.example.estoque_api.model.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public class UserEntitySpec {

    public static Specification<UserEntity> likeUsername(String username) {
        return (r, q, cb) -> cb.like(cb.lower(r.get("username")), username.toLowerCase() + "%");
    }
}
