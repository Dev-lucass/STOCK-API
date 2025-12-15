package com.example.estoque_api.service;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.InventoryEntityRepository;
import com.example.estoque_api.validation.InventoryEntityValidation;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryEntityService {

    private final InventoryEntityRepository repository;
    private final InventoryEntityValidation validation;
    private final HistoryEntityService historyService;

    public InventoryEntityService(InventoryEntityRepository repository, InventoryEntityValidation validation, HistoryEntityService historyService) {
        this.repository = repository;
        this.validation = validation;
        this.historyService = historyService;
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
        existingInventory.setQuantity(inventoryUpdated.getQuantity());
        return repository.save(existingInventory);
    }

    @Transactional
    public void deleteById(Long id) {
        InventoryEntity inventoryEntityFounded = validation.validationInventoryEntityIdIsValid(id);
        repository.delete(inventoryEntityFounded);
    }


    @Transactional
    public InventoryEntity takeFromInventory(UserEntity user, InventoryEntity order) {

        InventoryEntity inventory = repository.findByProduct(order.getProduct()).orElseThrow(() -> new ResourceNotFoundException("Product not found in inventory"));

        if (inventory.getQuantity() < order.getQuantity())
            throw new IllegalArgumentException("Not enough stock available");

        inventory.setQuantity(inventory.getQuantity() - order.getQuantity());

        historyService.save(
                user,
                inventory.getProduct(),
                InventoryAction.TAKE,
                order.getQuantity()
        );

        return repository.save(inventory);
    }

    @Transactional
    public InventoryEntity returnFromInventory(UserEntity user, InventoryEntity returnOrder) {

        InventoryEntity inventory = repository.findByProduct(returnOrder.getProduct()).orElseThrow(() -> new ResourceNotFoundException("Product not found in inventory"));
        inventory.setQuantity(inventory.getQuantity() + returnOrder.getQuantity());

        historyService.save(
                user,
                inventory.getProduct(),
                InventoryAction.RETURN,
                returnOrder.getQuantity()
        );

        return repository.save(inventory);
    }
}
