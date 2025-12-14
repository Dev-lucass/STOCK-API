package com.example.estoque_api.service;

import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.repository.HistoryEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistoryEntityService {

    private final HistoryEntityRepository repository;

    public HistoryEntityService(HistoryEntityRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public HistoryEntity save(HistoryEntity history) {
        return repository.save(history);
    }

    @Transactional(readOnly = true)
    public List<HistoryEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<HistoryEntity> findByUserId(Long userId) {
        return repository.findByUser_Id(userId);
    }

    @Transactional(readOnly = true)
    public List<HistoryEntity> findByProductId(Long productId) {
        return repository.findByProduct_Id(productId);
    }

}
