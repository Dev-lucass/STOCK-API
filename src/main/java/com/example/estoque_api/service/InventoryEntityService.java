package com.example.estoque_api.service;

import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.repository.InventoryEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryEntityService {

    private final InventoryEntityRepository repository;

    public InventoryEntityService(InventoryEntityRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public InventoryEntity save(InventoryEntity inventory) {
        return repository.save(inventory);
    }

    public List<InventoryEntity> findAll() {
        return repository.findAll();
    }

    @Transactional
    public InventoryEntity update(Long id, InventoryEntity inventoryUpdated) {

        InventoryEntity inventoryEntityFounded = repository.findById(id).orElseThrow(() -> new RuntimeException("Id inventory not found ..."));

        inventoryEntityFounded.setProduct(inventoryUpdated.getProduct());
        inventoryEntityFounded.setQuantity(inventoryEntityFounded.getQuantity());

        return inventoryEntityFounded;
    }

    @Transactional
    public void deleteById(Long id) {
        InventoryEntity inventoryEntityFounded = repository.findById(id).orElseThrow(() -> new RuntimeException("Id inventory not found ..."));
        repository.delete(inventoryEntityFounded);
    }

}
