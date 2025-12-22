package com.example.estoque_api.repository;

import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryEntityRepository extends JpaRepository<InventoryEntity, Long> {
    Boolean existsByProduct(ProductEntity product);
    Boolean existsByProductAndNot(ProductEntity product, Long id);
}
