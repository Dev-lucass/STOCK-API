package com.example.estoque_api.validation;

import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.repository.ProductEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidationProductId {

    private final ProductEntityRepository repository;

    public ValidationProductId(ProductEntityRepository repository) {
        this.repository = repository;
    }

    public ProductEntity validationIsValidId(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }
}
