package com.example.estoque_api.repository;

import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.HistoryId;
import com.example.estoque_api.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HistoryEntityRepository extends JpaRepository<HistoryEntity, HistoryId> {
    Optional<HistoryEntity> findByUser (UserEntity user);
}
