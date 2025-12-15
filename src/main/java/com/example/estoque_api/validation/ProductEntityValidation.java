package com.example.estoque_api.validation;

import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.repository.ProductEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityValidation {

    private final ProductEntityRepository repository;

    public ProductEntityValidation(ProductEntityRepository repository) {
        this.repository = repository;
    }

    public void validationProductEntityIsDuplicatedOnCreate(ProductEntity product) {
        repository.findByName(product.getName())
                .ifPresent(userFounded -> {
                    throw new DuplicateResouceException("PRODUCT already registered");
                });
    }

    public void validationProductEntityIsDuplicatedOnUpdate(ProductEntity product) {
        repository.findByName(product.getName())
                .ifPresent(productFounded -> {
                    if (!productFounded.getId().equals(product.getId())) throw new DuplicateResouceException("PRODUCT already registered");
                });
    }

    public ProductEntity validationProductEntityIdIsValid(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid product ID"));
    }
}
