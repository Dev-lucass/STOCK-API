package com.example.estoque_api.repository;

import com.example.estoque_api.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, Long> {
    Boolean existsByName(String name);
    Boolean existsByNameAndNot(String name, Long id);
    List<ProductEntity> findAllByActiveTrue();
}
