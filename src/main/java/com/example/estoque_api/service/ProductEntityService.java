package com.example.estoque_api.service;

import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.repository.ProductEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductEntityService {

    private final ProductEntityRepository repository;

    public ProductEntityService(ProductEntityRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ProductEntity save(ProductEntity product) {
        return repository.save(product);
    }

    public List<ProductEntity> findAll() {
        return repository.findAll();
    }

    @Transactional
    public ProductEntity update(Long id, ProductEntity productUpdated) {

        ProductEntity productEntityFounded = repository.findById(id).orElseThrow(() -> new RuntimeException("Id product not found ..."));

        productEntityFounded.setName(productUpdated.getName());
        productEntityFounded.setActive(productUpdated.getActive());

        return productEntityFounded;
    }

    @Transactional
    public void deleteById(Long id) {
        ProductEntity productEntityFounded = repository.findById(id).orElseThrow(() -> new RuntimeException("Id product not found ..."));
        repository.delete(productEntityFounded);
    }

}
