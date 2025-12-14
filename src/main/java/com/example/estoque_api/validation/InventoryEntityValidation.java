package com.example.estoque_api.validation;

import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.repository.InventoryEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class InventoryEntityValidation {

    private final InventoryEntityRepository repository;

    public InventoryEntityValidation(InventoryEntityRepository repository) {
        this.repository = repository;
    }

    public void validationInventoryEntityIsDuplicatedOnCreate(InventoryEntity inventory) {
        repository.findByProduct(inventory.getProduct())
                .ifPresent(userFounded -> {
                    throw new DuplicateResouceException("PRODUCT already registered in inventory");
                });
    }

    public void validationInventoryEntityIsDuplicatedOnUpdate(InventoryEntity inventory) {
        repository.findByProduct(inventory.getProduct())
                .ifPresent(productFounded -> {
                    if (!productFounded.getId().equals(inventory.getId()))
                        throw new DuplicateResouceException("PRODUCT already registered in inventory");
                });
    }

    public InventoryEntity validationInventoryEntityIdIsValid(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid ID"));
    }


}
