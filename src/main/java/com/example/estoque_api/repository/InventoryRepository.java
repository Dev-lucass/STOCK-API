package com.example.estoque_api.repository;

import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long>, JpaSpecificationExecutor<InventoryEntity> {
    boolean existsByTool(ToolEntity tool);
    boolean existsByToolAndIdNot(ToolEntity tool, Long id);
    List<InventoryEntity> findAllByToolActiveTrue();
    Optional<InventoryEntity> findByInventoryId(UUID inventoryId);
}
