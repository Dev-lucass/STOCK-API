package com.example.estoque_api.service;

import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
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
    private final ProductEntityService productService;

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

    private void validationInventoryProductIsDuplicatedOnCreate(InventoryEntity inventory) {
        if (repository.existsByProduct(inventory.getProduct()))
            throw new DuplicateResouceException("Product already registered in inventory");
    }

    private void validateInventoryProductIsDuplicatedOnUpdate(InventoryEntity inventory) {
        if (repository.existsByProductAndNot(
                inventory.getProduct(),
                inventory.getId())
        ) throw new DuplicateResouceException("Product already registered in inventory");
    }

    private InventoryEntity findInventoryByIdOrElseThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid inventory id"));
    }
}
