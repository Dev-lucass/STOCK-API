package com.example.estoque_api.repository;

import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.HistoryId;
import com.example.estoque_api.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface HistoryEntityRepository extends JpaRepository<HistoryEntity, HistoryId> , JpaSpecificationExecutor<HistoryEntity> {
    Optional<HistoryEntity> findByUser (UserEntity user);
}
