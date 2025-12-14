package com.example.estoque_api.repository;

import com.example.estoque_api.model.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryEntityRepository extends JpaRepository<InventoryEntity, Long> {
}
