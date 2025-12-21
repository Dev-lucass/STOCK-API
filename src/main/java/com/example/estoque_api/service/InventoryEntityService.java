package com.example.estoque_api.service;

import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.InventoryEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryEntityService {

    private final InventoryEntityRepository repository;
    private final HistoryEntityService historyService;

    public InventoryEntity save(InventoryEntity inventory) {
        return null;
    }

    public List<InventoryEntity> findAll() {
        return null;
    }

    public InventoryEntity update(Long id, InventoryEntity inventoryUpdated) {
        return null;
    }

    public void deleteById(Long id) {

    }

    public InventoryEntity takeFromInventory(UserEntity user, InventoryEntity order) {
        return null;
    }

    public InventoryEntity returnFromInventory(UserEntity user, InventoryEntity returnOrder) {
        return null;
    }
}
