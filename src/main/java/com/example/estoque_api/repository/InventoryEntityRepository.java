package com.example.estoque_api.repository;

import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Optional;

public interface InventoryEntityRepository extends JpaRepository<InventoryEntity, Long>, JpaSpecificationExecutor<InventoryEntity> {
    Boolean existsByTool(ToolEntity product);
    Boolean existsByToolAndIdNot(ToolEntity product, Long id);
    List<InventoryEntity> findAllByToolActiveTrue();
    Optional<InventoryEntity> findByInventoryId(String inventoryId);
}
