package com.example.estoque_api.service;

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

        InventoryEntity inventoryEntityFounded = validation.validationInventoryEntityIdIsValid(id);
        validation.validationInventoryEntityIsDuplicatedOnUpdate(inventoryUpdated);

        inventoryEntityFounded.setProduct(inventoryUpdated.getProduct());
        inventoryEntityFounded.setQuantity(inventoryEntityFounded.getQuantity());

        return inventoryEntityFounded;
    }

    @Transactional
    public void deleteById(Long id) {
        InventoryEntity inventoryEntityFounded = validation.validationInventoryEntityIdIsValid(id);
        repository.delete(inventoryEntityFounded);
    }

}
