package com.example.estoque_api.repository;

import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.HistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryEntityRepository extends JpaRepository<HistoryEntity, HistoryId> {

    List<HistoryEntity> findByUser_Id(Long userId);

    List<HistoryEntity> findByProduct_Id(Long productId);

}
