package com.example.estoque_api.validation;

import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.repository.InventoryEntityRepository;
import com.example.estoque_api.repository.ProductEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class InventoryEntityValidation {

    private final InventoryEntityRepository repository;
    private final ProductEntityRepository productEntityRepository;

    public InventoryEntityValidation(InventoryEntityRepository repository, ProductEntityRepository productEntityRepository) {
        this.repository = repository;
        this.productEntityRepository = productEntityRepository;
    }

    @Transactional
    public void validationInventoryEntityIsDuplicatedOnCreate(InventoryEntity inventory) {

        var productFound = productEntityRepository.findById(inventory.getProduct().getId()).get();

        repository.findByProduct(productFound)
                .ifPresent(userFounded -> {
                    throw new DuplicateResouceException("PRODUCT already registered in inventory");
                });
    }


    public InventoryEntity validationInventoryEntityIdIsValid(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid ID"));
    }


}
