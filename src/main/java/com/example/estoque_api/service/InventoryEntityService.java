package com.example.estoque_api.service;

import com.example.estoque_api.dto.response.InventoryEntityResponseDTO;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.repository.InventoryEntityRepository;
import com.example.estoque_api.validation.InventoryEntityValidation;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        inventoryEntityFounded.setQuantity(inventoryUpdated.getQuantity());

        return inventoryEntityFounded;
    }

    @Transactional
    public void deleteById(Long id) {
        InventoryEntity inventoryEntityFounded = validation.validationInventoryEntityIdIsValid(id);
        repository.delete(inventoryEntityFounded);
    }

    @Transactional
    public InventoryEntityResponseDTO takeFromInventory(Long productId, Integer quantity) {

        InventoryEntity inventory = repository.findByProduct_Id(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found in inventory"));

        if (inventory.getQuantity() < quantity) throw new IllegalArgumentException("Not enough stock available");

        inventory.setQuantity(inventory.getQuantity() - quantity);
        InventoryEntity updatedInventory = repository.save(inventory);

        return new InventoryEntityResponseDTO(
                updatedInventory.getQuantity(),
                updatedInventory.getProduct().getId(),
                LocalDateTime.now()
        );
    }


    @Transactional
    public InventoryEntityResponseDTO returnFromInventory(Long productId, Integer quantity) {

        InventoryEntity inventory = repository.findByProduct_Id(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found in inventory"));

        inventory.setQuantity(inventory.getQuantity() + quantity);
        InventoryEntity updatedInventory = repository.save(inventory);

        return new InventoryEntityResponseDTO(
                updatedInventory.getQuantity(),
                updatedInventory.getProduct().getId(),
                LocalDateTime.now()
        );
    }

}
