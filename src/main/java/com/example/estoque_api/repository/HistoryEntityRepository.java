package com.example.estoque_api.repository;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface HistoryEntityRepository extends JpaRepository<HistoryEntity, Long>, JpaSpecificationExecutor<HistoryEntity> {
    boolean existsByUserAndAction(UserEntity user, InventoryAction action);
    List<HistoryEntity> findByUser(UserEntity user);
}
