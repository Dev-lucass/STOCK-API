package com.example.estoque_api.repository;

import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.HistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryEntityRepository extends JpaRepository<HistoryEntity, HistoryId> {}
