package com.example.estoque_api.service;

import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.repository.InventoryEntityRepository;
import com.example.estoque_api.validation.InventoryEntityValidation;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryEntityService {

    private final InventoryEntityRepository repository;
    private final InventoryEntityValidation validation;

    public InventoryEntityService(InventoryEntityRepository repository, InventoryEntityValidation validation) {
        this.repository = repository;
        this.validation = validation;
    }

    @Transactional
    public InventoryEntity save(InventoryEntity inventory) {
        validation.validationInventoryEntityIsDuplicatedOnCreate(inventory);
        return repository.save(inventory);
    }

    public List<InventoryEntity> findAll() {
        return repository.findAll();
    }

    @Transactional
    public InventoryEntity update(Long id, InventoryEntity inventoryUpdated) {
        InventoryEntity existingInventory = validation.validationInventoryEntityIdIsValid(id);
        validation.validationInventoryEntityIsDuplicatedOnUpdate(inventoryUpdated);

        existingInventory.setQuantity(inventoryUpdated.getQuantity());
        return repository.save(existingInventory);
    }

    @Transactional
    public void deleteById(Long id) {
        InventoryEntity inventoryEntityFounded = validation.validationInventoryEntityIdIsValid(id);
        repository.delete(inventoryEntityFounded);
    }

    @Transactional
    public InventoryEntity takeFromInventory(InventoryEntity order) {
        InventoryEntity inventory = repository.findByProduct_Id(order.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in inventory"));

        if (inventory.getQuantity() < order.getQuantity())
            throw new IllegalArgumentException("Not enough stock available");

        inventory.setQuantity(inventory.getQuantity() - order.getQuantity());
        return repository.save(inventory);
    }

    @Transactional
    public InventoryEntity returnFromInventory(InventoryEntity returnOrder) {
        InventoryEntity inventory = repository.findByProduct_Id(returnOrder.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in inventory"));
        inventory.setQuantity(inventory.getQuantity() + returnOrder.getQuantity());
        return repository.save(inventory);
    }
}
