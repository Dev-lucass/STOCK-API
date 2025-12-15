package com.example.estoque_api.service;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
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
    public void save(UserEntity user, ProductEntity product, InventoryAction action, int quantity) {

        HistoryEntity history = new HistoryEntity(
                user,
                product,
                action,
                quantity
        );

        repository.save(history);
    }

    @Transactional(readOnly = true)
    public List<HistoryEntity> findAll() {
        return repository.findAll();
    }
}
